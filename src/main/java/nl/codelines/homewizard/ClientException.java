package nl.codelines.homewizard;

public class ClientException extends RuntimeException {
    private final int statusCode;
    private final String body;

    public ClientException(String uri, int statusCode, String body) {
        super("Error calling GET " + uri);
        this.statusCode = statusCode;
        this.body = body;
    }
}
