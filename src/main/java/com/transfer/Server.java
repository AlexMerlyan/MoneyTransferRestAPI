package com.transfer;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.transfer.dao.AccountDao;
import com.transfer.dao.AccountDaoImpl;
import com.transfer.service.MoneyTransferService;
import com.transfer.service.MoneyTransferServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    private static final String TRANSFER_PATH = "/transfer";

    public static void main(String[] args) throws IOException {
        runServer();
    }

    public static void runServer() throws IOException {
        AccountDao accountDao = new AccountDaoImpl();
        MoneyTransferService service = new MoneyTransferServiceImpl(accountDao);
        MoneyTransferHandler handler = new MoneyTransferHandler(service);

        runServer(handler);
    }

    public static void runServer(HttpHandler handler) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext(TRANSFER_PATH, handler);
        server.setExecutor(null); // creates a default executor
        server.start();
    }

}
