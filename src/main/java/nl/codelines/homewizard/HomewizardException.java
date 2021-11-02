package nl.codelines.homewizard;

public class HomewizardException extends RuntimeException {
    private final int status;
    private final int code;
    private final String description;
    private final String operation;

    public HomewizardException(int status, int code, String description, String operation) {
        super(String.format("Error %s on %s: %s - %s", status, operation, code, operation));
        this.status = status;
        this.code = code;
        this.description = description;
        this.operation = operation;
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "HomewizardException{" +
            "status=" + status +
            ", code=" + code +
            ", description='" + description + '\'' +
            ", operation='" + operation + '\'' +
            '}';
    }
}
