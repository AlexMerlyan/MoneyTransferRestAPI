package com.transfer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.transfer.dto.MoneyTransferDTO;
import com.transfer.dto.ResponseDTO;
import com.transfer.service.MoneyTransferService;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Scanner;

@Data
@AllArgsConstructor
public class MoneyTransferHandler implements HttpHandler {

    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";

    private static final String WRONG_JSON = "Malformed json. Please, correct it and try again.";
    private static final String METHOD_IS_NOT_SUPPORTED = "Http method is not supported. Please, try to use POST.";

    private static final String POST = "POST";
    private static final int SUCCESS_CODE = 200;
    private static final int FAILURE_CODE = 400;

    private Gson gson;
    private MoneyTransferService transferService;

    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (Objects.equals(method, POST)) {
            transferMoney(exchange);
        } else {
            writeResponse(exchange, gson, new ResponseDTO(METHOD_IS_NOT_SUPPORTED, false));
        }
        exchange.close();
    }

    private void transferMoney(HttpExchange exchange) throws IOException {
        MoneyTransferDTO dto;
        try (Scanner scanner = new Scanner(exchange.getRequestBody())) {
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            dto = gson.fromJson(sb.toString(), MoneyTransferDTO.class);
        } catch (JsonSyntaxException e) {
            writeResponse(exchange, gson, new ResponseDTO(WRONG_JSON, false));
            return;
        }

        ResponseDTO response = transferService.transfer(dto);
        writeResponse(exchange, gson, response);
        exchange.close();
    }

    private void writeResponse(HttpExchange exchange, Gson gson, ResponseDTO response) throws IOException {
        int code = response.isSuccessful() ? SUCCESS_CODE : FAILURE_CODE;
        String json = gson.toJson(response);
        byte[] jsonBytes = json.getBytes();

        exchange.getResponseHeaders().add(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);
        exchange.sendResponseHeaders(code, jsonBytes.length);
        try (OutputStream responseBody = exchange.getResponseBody()) {
            responseBody.write(jsonBytes);
        }
    }

}