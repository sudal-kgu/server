package store.sonyk9919.api.domain.analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestIdDto {

    @JsonProperty("request_id")
    private final String requestId;

    public static RequestIdDto from(String requestId){
        return new RequestIdDto(requestId);
    }
}
