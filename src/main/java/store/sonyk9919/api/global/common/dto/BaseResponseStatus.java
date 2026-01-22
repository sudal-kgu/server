package store.sonyk9919.api.global.common.dto;

import org.springframework.http.HttpStatus;

public interface BaseResponseStatus {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
