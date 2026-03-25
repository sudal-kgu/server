package store.sonyk9919.api.domain.trash.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmResponseDto {
    private final String serial;

    public static ConfirmResponseDto from(String serial) {
        return new ConfirmResponseDto(serial);
    }
}