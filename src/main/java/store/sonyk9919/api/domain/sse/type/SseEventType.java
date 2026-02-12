package store.sonyk9919.api.domain.sse.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SseEventType {
    CONNECT("connect"),
    ANALYSIS_RESULT("analysis-result"),
    ERROR("error");

    private final String value;
}
