package store.sonyk9919.api.domain.auth.utils;

public class RequestHeaderUtils {
    public static String getBearerToken(String token) {
        return "Bearer " + token;
    }
}
