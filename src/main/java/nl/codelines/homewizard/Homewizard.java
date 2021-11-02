package nl.codelines.homewizard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.codelines.homewizard.model.Measurement;
import nl.codelines.homewizard.model.ProductInfo;
import nl.codelines.homewizard.model.State;
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

    public Measurement getMeasurement() {
        return doGetJson(baseUrl + "/api/v1/data", Measurement.class);
    }

    public ProductInfo getProductInfo() {
        return doGetJson(baseUrl + "/api", ProductInfo.class);
    }

    public Telegram getTelegram() {
        return new Telegram(doGetString(baseUrl + "/api/v1/telegram"));
    }

    public State getState() {
        return doGetJson(baseUrl + "/api/v1/state", State.class);
    }

    public State setState(Boolean powerOn, Boolean switchLock, Integer brightness) {
        return doPutJson(baseUrl + "/api/v1/state", new StateUpdate(powerOn, switchLock, brightness), State.class);
    }

    private <T> T doGetJson(String uri, Class<T> clazz) {
        try {
            var res = client.send(get(uri), HttpResponse.BodyHandlers.ofByteArray());
            if (res.statusCode() >= 400) {
                parseAndThrow(res, mapper.readValue(res.body(), ErrorResponse.class));
            }
            return mapper.readValue(res.body(), clazz);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T doPutJson(String uri, Object value, Class<T> clazz) {
        try {
            var bytes = mapper.writeValueAsBytes(value);
            var res = client.send(put(uri, bytes), HttpResponse.BodyHandlers.ofByteArray());
            if (res.statusCode() >= 400) {
                parseAndThrow(res, mapper.readValue(res.body(), ErrorResponse.class));
            }
            return mapper.readValue(res.body(), clazz);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String doGetString(String uri) {
        try {
            var res = client.send(get(uri), HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() >= 400) {
                parseAndThrow(res, mapper.readValue(res.body(), ErrorResponse.class));
            }
            return res.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static final HttpRequest get(String uri) {
        return HttpRequest.newBuilder(URI.create(uri))
            .GET()
            .header("Accept", "application/json")
            .build();
    }

    private static final HttpRequest put(String uri, byte[] value) {
        return HttpRequest.newBuilder(URI.create(uri))
            .PUT(HttpRequest.BodyPublishers.ofByteArray(value))
            .header("Accept", "application/json")
            .build();
    }

    private static HttpClient defaultClient() {
        return HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    }

    private void parseAndThrow(HttpResponse<?> res, ErrorResponse error) {
        var operation = String.format("%s %s", res.request().method(), res.request().uri().toString());

        throw new HomewizardException(
            res.statusCode(),
            error.error.id, error.error.description, operation);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class StateUpdate {
        @JsonProperty("power_on")
        public final Boolean powerOn;
        @JsonProperty("switch_lock")
        public final Boolean switchLock;
        @JsonProperty("brightness")
        public final Integer brightness;

        private StateUpdate(Boolean powerOn, Boolean switchLock, Integer brightness) {
            this.powerOn = powerOn;
            this.switchLock = switchLock;
            this.brightness = brightness;
        }
    }

    static class ErrorResponse {
        private final ErrorBody error;

        ErrorResponse(@JsonProperty("error") ErrorBody error) {
            this.error = error;
        }

        static class ErrorBody {
            private final int id;
            private final String description;

            ErrorBody(@JsonProperty("id") int id, @JsonProperty("description") String description) {
                this.id = id;
                this.description = description;
            }
        }
    }
}
