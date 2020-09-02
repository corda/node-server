package net.corda.explorer.model.response;

public class LogBody {
    private final String message;
    private final String object;

    public String getMessage() {
        return message;
    }

    public String getObject() {
        return object;
    }

    public LogBody(String message, String object) {
        this.message = message;
        this.object = object;
    }
}
