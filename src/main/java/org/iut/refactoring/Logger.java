
package org.iut.refactoring;

import java.time.LocalDateTime;
import java.util.*;

public class Logger {
    private final List<String> logs = new ArrayList<>();

    public void log(String message) {
        String entry = LocalDateTime.now() + " - " + message;
        logs.add(entry);
    }

    public List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }
}
