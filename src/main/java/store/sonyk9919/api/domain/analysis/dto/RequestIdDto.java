package store.sonyk9919.api.domain.analysis.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestIdDto {
    private final String requestId;

    public static RequestIdDto from(String requestId){
        return new RequestIdDto(requestId);
    }
}
