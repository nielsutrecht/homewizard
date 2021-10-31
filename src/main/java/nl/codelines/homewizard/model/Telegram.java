package nl.codelines.homewizard.model;

public class Telegram {
    private final String data;

    public Telegram(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Telegram{" +
                "data='" + data + '\'' +
                '}';
    }
}
