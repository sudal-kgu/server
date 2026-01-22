package store.sonyk9919.api.global.common.exception;

import lombok.Getter;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
public class CustomException extends RuntimeException {
    private final BaseResponseStatus status;

    public CustomException(BaseResponseStatus status){
        super(status.getMessage());
        this.status = status;
    }

    public CustomException(BaseResponseStatus status, String message){
        super(message);
        this.status = status;
    }
}
