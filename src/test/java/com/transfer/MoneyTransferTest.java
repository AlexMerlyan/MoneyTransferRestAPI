package com.transfer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import com.transfer.dao.AccountDao;
import com.transfer.dao.AccountDaoImpl;
import com.transfer.dto.MoneyTransferDTO;
import com.transfer.dto.ResponseDTO;
import com.transfer.handler.MoneyTransferHandler;
import com.transfer.model.Account;
import com.transfer.service.MoneyTransferServiceImpl;
import javafx.util.Pair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import static com.transfer.service.MoneyTransferServiceImpl.SUCCESS_TRANSFER;
import static org.junit.Assert.assertEquals;

public class MoneyTransferTest {

    private static final String POST_REQUEST_ADDRESS = "http://localhost:8000/transfer";

    private static HttpServer server;
    private static AccountDao dao;
    private static MoneyTransferServiceImpl service;

    private Gson gson = new Gson();

    @BeforeClass
    public static void initAndStartServer() throws IOException {
        dao = new AccountDaoImpl();
        service = new MoneyTransferServiceImpl(dao);
        MoneyTransferHandler handler = new MoneyTransferHandler(new Gson(), service);
        server = Server.createServer(handler);
        server.start();
    }

    @AfterClass
    public static void stopServer() {
        server.stop(0);
    }

    @Before
    public void clearDatabase() {
        dao = new AccountDaoImpl();
        service.setAccountDao(dao);
    }

    @Test
    public void shouldReturnAccountsNotExists() throws IOException {
        //given
        String requestData = gson.toJson(new MoneyTransferDTO(1, 2, 100_99L));
        HttpPost request = createRequest(requestData);

        //when
        Pair<ResponseDTO, Integer> pair = sendRequest(request);

        //then
        Integer expectedCode = 400;
        ResponseDTO expectedContent = new ResponseDTO("Accounts with ids 1, 2 does not exist", false);

        assertEquals(expectedContent, pair.getKey());
        assertEquals(expectedCode, pair.getValue());
    }

    @Test
    public void shouldReturnNotEnoughMoney() throws IOException {
        //given
        String requestData = gson.toJson(new MoneyTransferDTO(1, 2, 100_99L));
        HttpPost request = createRequest(requestData);

        dao.saveAccount(new Account(1, 50_99));
        dao.saveAccount(new Account(2, 150_00));

        //when
        Pair<ResponseDTO, Integer> pair = sendRequest(request);

        //then
        ResponseDTO expectedContent = new ResponseDTO("Not enough money on account with id 1", false);
        Integer expectedCode = 400;

        assertEquals(expectedContent, pair.getKey());
        assertEquals(expectedCode, pair.getValue());
    }

    @Test
    public void shouldMakeTransferSuccessfully() throws IOException {
        //given
        String requestData = gson.toJson(new MoneyTransferDTO(1, 2, 100_99L));
        HttpPost request = createRequest(requestData);

        dao.saveAccount(new Account(1, 200_98));
        dao.saveAccount(new Account(2, 150_05));

        //when
        Pair<ResponseDTO, Integer> pair = sendRequest(request);

        //then
        ResponseDTO expectedContent = new ResponseDTO(SUCCESS_TRANSFER, true);
        Integer expectedCode = 200;
        Account firstAccount = new Account(1, 99_99);
        Account secondAccount = new Account(2, 251_04);

        assertEquals(expectedContent, pair.getKey());
        assertEquals(expectedCode, pair.getValue());
        assertEquals(firstAccount, dao.getAccountById(1));
        assertEquals(secondAccount, dao.getAccountById(2));
    }

    private HttpPost createRequest(String requestData) throws UnsupportedEncodingException {
        HttpPost request = new HttpPost(POST_REQUEST_ADDRESS);
        StringEntity params = new StringEntity(requestData);
        request.addHeader(MoneyTransferHandler.CONTENT_TYPE_KEY, MoneyTransferHandler.CONTENT_TYPE_VALUE);
        request.setEntity(params);
        return request;
    }

    private Pair<ResponseDTO, Integer> sendRequest(HttpUriRequest request) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request);
             Scanner scanner = new Scanner(response.getEntity().getContent())) {
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            ResponseDTO actualContent = gson.fromJson(sb.toString(), ResponseDTO.class);
            Integer actualCode = response.getStatusLine().getStatusCode();
            return new Pair<>(actualContent, actualCode);
        }
    }
}
