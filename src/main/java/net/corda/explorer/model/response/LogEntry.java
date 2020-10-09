package net.corda.explorer.model.response;

public class LogEntry {
    private final String severity;
    private final String date;
    private final String thread;
    private final String source;
    private final LogBody body;

    public LogEntry(String severity, String date, String thread, String source, LogBody body) {
        this.severity = severity;
        this.date = date;
        this.thread = thread;
        this.source = source;
        this.body = body;
    }

    public String getSeverity() {
        return severity;
    }

    public String getDate() {
        return date;
    }

    public String getThread() {
        return thread;
    }

    public String getSource() {
        return source;
    }

    public LogBody getBody() {
        return body;
    }
}