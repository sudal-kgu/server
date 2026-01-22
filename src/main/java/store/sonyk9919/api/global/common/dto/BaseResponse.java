package store.sonyk9919.api.global.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@JsonPropertyOrder({"isSuccess", "code", "message", "data"})
public class BaseResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    private final T data;

    private BaseResponse(Boolean isSuccess, BaseResponseStatus status, T data) {
        this.isSuccess = isSuccess;
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = data;
    }

    private BaseResponse(Boolean isSuccess, BaseResponseStatus status, String customMessage, T data) {
        this.isSuccess = isSuccess;
        this.code = status.getCode();
        this.message = customMessage;
        this.data = data;
    }

    public static ResponseEntity<BaseResponse<Void>> success() {
        BaseResponseStatus success = SuccessStatus.SUCCESS;

        return ResponseEntity
                .status(success.getHttpStatus())
                .body(new BaseResponse<>(true, success, null));
    }

    public static <T> ResponseEntity<BaseResponse<T>> success(T data) {
        BaseResponseStatus success = SuccessStatus.SUCCESS;

        return ResponseEntity
                .status(success.getHttpStatus())
                .body(new BaseResponse<>(true, success, data));
    }

    public static <T> ResponseEntity<BaseResponse<T>> success(BaseResponseStatus status, T data) {
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(new BaseResponse<>(true, status, data));
    }

    public static ResponseEntity<BaseResponse<Void>> error(BaseResponseStatus status) {
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(new BaseResponse<>(false, status, null));
    }

    public static ResponseEntity<BaseResponse<Void>> error(BaseResponseStatus status, String customMessage) {
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(new BaseResponse<>(false, status, customMessage, null));
    }

    public static <T> ResponseEntity<BaseResponse<T>> error(BaseResponseStatus status, T data) {
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(new BaseResponse<>(false, status, data));
    }
}
