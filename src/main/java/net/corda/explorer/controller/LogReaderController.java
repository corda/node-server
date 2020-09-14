package net.corda.explorer.controller;

import net.corda.explorer.model.request.EntriesCountRequest;
import net.corda.explorer.model.request.ReadRequest;
import net.corda.explorer.model.response.EntriesCountResponse;
import net.corda.explorer.model.response.LogEntries;
import net.corda.explorer.model.response.LogEntry;
import net.corda.explorer.service.StringToEntry;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@CrossOrigin(origins = "*")
@RestController
public class LogReaderController {
    @PostMapping("logReader/read")
    public LogEntries getLogEntries(@NotNull @RequestBody ReadRequest readRequest) {
        final File logFile = new File(pathFromComponents(readRequest.getComponents()));
        final List<LogEntry> entries = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        int entriesSeen = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            for (String line; (line = reader.readLine()) != null && entriesSeen <= readRequest.getStopIndex(); ) {
                if (isStartOfLog(line)) {
                    if (stringBuilder.length() != 0 && entriesSeen >= readRequest.getStartIndex()) {
                        entries.add(StringToEntry.parse(stringBuilder.toString()));
                    }
                    stringBuilder = new StringBuilder(line);
                    entriesSeen++;
                }
                else stringBuilder.append(line);
            }
        }
        catch (IOException | ParseException ex) { ex.printStackTrace(); }

        Collections.reverse(entries); // so that the most recent entries are at the top
        return new LogEntries(entries);
    }

    @PostMapping("logReader/entriesCount")
    public EntriesCountResponse getEntriesCount(@NotNull @RequestBody EntriesCountRequest request) {
        final File logFile = new File(pathFromComponents(request.getComponents()));
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            for (String line; (line = reader.readLine()) != null;) {
                if (isStartOfLog(line)) { count++; }
            }
        }
        catch (IOException ex) { ex.printStackTrace(); }
        return new EntriesCountResponse(count);
    }

    private boolean isStartOfLog(String line) {
        return Stream.of("[INFO ]", "[WARN ]", "[ERROR]").anyMatch(line::startsWith);
    }

    private String pathFromComponents(List<String> components) {
        return components.stream().reduce(
                "", //System.getProperty("user.home"),
                (acc, next) -> acc + File.separator + next
        );
    }
}
