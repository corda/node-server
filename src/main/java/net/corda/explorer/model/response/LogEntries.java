package net.corda.explorer.model.response;

import java.util.List;

public class LogEntries {
    private final List<LogEntry> entries;

    public List<LogEntry> getEntries() { return entries; }

    public LogEntries(List<LogEntry> entries) { this.entries = entries; }
}
