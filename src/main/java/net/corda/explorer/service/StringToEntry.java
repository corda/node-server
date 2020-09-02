package net.corda.explorer.service;

import net.corda.explorer.model.response.LogBody;
import net.corda.explorer.model.response.LogEntry;

import javax.json.Json;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringToEntry {
    public static LogEntry parse(String line) throws ParseException {
        final String regex = "\\[(.+)\\] (.+) \\[(.+)\\] (.+) - (.+)";
        final Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(line);
        if (matcher.find()) {
            return new LogEntry(
                    matcher.group(1).trim(),
                    matcher.group(2),
                    matcher.group(3),
                    matcher.group(4),
                    strToLogBody(matcher.group(5))
            );
        }
        throw new ParseException("Could not match regex\n" + regex + "\non string\n" + line, 0);
    }

    private static LogBody strToLogBody(String strBody) {
        final String regex = "([^{]+)(.*)";
        Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(strBody);
        if (matcher.find() && matcher.groupCount() == 2) {
            String jsonAttempt = translateToJSON(matcher.group(2));
            if (jsonAttempt != null) { return new LogBody(matcher.group(1), jsonAttempt); }
        }
        return new LogBody(strBody, "{}");
    }

    private static String translateToJSON(String encodedObject) {
        if (isValidJSON(encodedObject)) { return encodedObject; }

        String keyValueAttempt = keyEqualsValueToJSON(encodedObject);
        if (isValidJSON(keyValueAttempt)) { return keyValueAttempt; }

        return null;
    }

    private static String keyEqualsValueToJSON(String keyEqualsValueEncoding) {
        return keyEqualsValueEncoding
                .replace("\"", "\\\"")
                .replace("{", "{\"")
                .replace("}", "\"}")
                .replace(" = ", "=")
                .replace("=", "\": \"")
                .replace(", ", ",")
                .replace(",", "\", \"");
    }

    private static boolean isValidJSON(String strObject) {
        try {
            Json.createReader(new StringReader(strObject)).readObject();
            return true;
        }
        catch (Exception ignored) { return false; }
    }
}
