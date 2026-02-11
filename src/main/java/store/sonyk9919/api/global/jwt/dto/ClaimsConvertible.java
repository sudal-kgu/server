package store.sonyk9919.api.global.jwt.dto;

import java.util.Map;

public interface ClaimsConvertible {
    String getSubject();
    Map<String, Object> getBody();
}
