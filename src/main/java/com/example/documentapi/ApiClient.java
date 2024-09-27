package com.example.documentapi;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.net.URI;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/documents"; // Cambia esto a la URL de tu API

    public static void main(String[] args) {
        try {
            getDocuments();
            getDocumentsByUserId(1L);
            getDocumentsByBusinessId(1L);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void getDocuments() throws IOException, ParseException {
        String url = BASE_URL + "?startDate=2023-01-01T00:00:00&endDate=2023-12-31T23:59:59&userId=1&action=uploaded";
        executeGetRequest(url);
    }

    private static void getDocumentsByUserId(Long userId) throws IOException, ParseException {
        String url = BASE_URL + "?userId=" + userId;
        executeGetRequest(url);
    }

    private static void getDocumentsByBusinessId(Long businessId) throws IOException, ParseException {
        String url = BASE_URL + "/business/" + businessId;
        executeGetRequest(url);
    }

    private static void executeGetRequest(String url) throws IOException, ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(URI.create(url));
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                System.out.println("Response Code: " + response.getCode());
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response Body: " + responseBody);
            }
        }
    }
}
