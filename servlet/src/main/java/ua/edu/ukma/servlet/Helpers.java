package ua.edu.ukma.servlet;

import java.util.Optional;

class Helpers {
    private Helpers() {
    }

    static boolean hasIdInPath(String apiPrefix, String path) {
        if (path != null) {
            path = path.substring(1);

            String[] pathParts = path.split("/");

            String lastPart = pathParts[pathParts.length - 1];

            return !lastPart.equals(apiPrefix);
        }

        return false;
    }

    static Optional<Long> extractIdFromPath(String path) {
        if (path != null) {
            path = path.substring(1);

            String[] pathParts = path.split("/");

            String lastPart = pathParts[pathParts.length - 1];

            return Optional.of(Long.parseLong(lastPart));
        }

        return Optional.empty();
    }
}
