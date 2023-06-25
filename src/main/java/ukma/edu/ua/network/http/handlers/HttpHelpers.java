package ukma.edu.ua.network.http.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Long.parseLong;

class HttpHelpers {
    private HttpHelpers() {
    }

    static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();

        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    params.put(key, value);
                }
            }
        }

        return params;
    }

    public static long parseId(String path) throws IllegalArgumentException {
        String[] pathElements = path.split("/");
        String id = pathElements[pathElements.length - 1];

        try {
            return parseLong(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String readToString(InputStream input) throws IOException {
        return new String(input.readAllBytes(), StandardCharsets.UTF_8);
    }
}
