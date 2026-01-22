package store.sonyk9919.api.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseResponseStatus {

    SUCCESS(HttpStatus.OK,"COMMON_200","성공입니다."),
    CREATED(HttpStatus.CREATED, "COMMON_201", "생성되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
