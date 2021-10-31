package nl.codelines.homewizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.codelines.homewizard.model.Measurement;
import nl.codelines.homewizard.model.ProductInfo;
import nl.codelines.homewizard.model.Telegram;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Homewizard {
    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper mapper;

    public Homewizard(String baseUrl) {
        this(baseUrl, defaultClient());
    }

    public Homewizard(String baseUrl, HttpClient client) {
        this.baseUrl = baseUrl;
        this.client = client;
        this.mapper = new ObjectMapper();
    }

    public Measurement getMeasurement() throws IOException, InterruptedException {
        return doGetJson(baseUrl + "/api/v1/data", Measurement.class);
    }

    public ProductInfo getProductInfo() {
        return doGetJson(baseUrl + "/api", ProductInfo.class);
    }

    public Telegram getTelegram() {
        return new Telegram(doGetString(baseUrl + "/api/v1/telegram"));
    }

    private <T> T doGetJson(String uri, Class<T> clazz) {
        try {
            var res = client.send(get(uri), HttpResponse.BodyHandlers.ofByteArray());
            if(res.statusCode() >= 400) {
                throw new ClientException(uri, res.statusCode(), new String(res.body()));
            }
            return mapper.readValue(res.body(), clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String doGetString(String uri) {
        try {
            var res = client.send(get(uri), HttpResponse.BodyHandlers.ofString());
            if(res.statusCode() >= 400) {
                throw new ClientException(uri, res.statusCode(), res.body());
            }
            return res.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final HttpRequest get(String uri) {
        return HttpRequest.newBuilder(URI.create(uri))
                .GET()
                .header("Accept", "application/json")
                .build();
    }

    private static HttpClient defaultClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }
}
