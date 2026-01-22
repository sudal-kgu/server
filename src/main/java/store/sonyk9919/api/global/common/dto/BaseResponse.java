package store.sonyk9919.api.global.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@JsonPropertyOrder({"code", "message", "data"})
public class BaseResponse<T> {
    private final String code;
    private final String message;
    private final T data;

    private BaseResponse(BaseResponseStatus status, String message, T data) {
        this.code = status.getCode();
        this.message = message;
        this.data = data;
    }

    public static ResponseEntity<BaseResponse<Void>> success() {
        return build(SuccessStatus.SUCCESS, null, null);
    }

    public static <T> ResponseEntity<BaseResponse<T>> success(T data) {
        return build(SuccessStatus.SUCCESS, null, data);
    }

    public static <T> ResponseEntity<BaseResponse<T>> success(BaseResponseStatus status, T data) {
        return build(status, null, data);
    }

    public static ResponseEntity<BaseResponse<Void>> error(BaseResponseStatus status) {
        return build(status, null, null);
    }

    public static ResponseEntity<BaseResponse<Void>> error(BaseResponseStatus status, String customMessage) {
        return build(status, customMessage, null);
    }

    public static <T> ResponseEntity<BaseResponse<T>> error(BaseResponseStatus status, T data) {
        return build(status, null, data);
    }

    private static <T> ResponseEntity<BaseResponse<T>> build(BaseResponseStatus status, String customMessage, T data) {
        String message = (customMessage != null) ? customMessage : status.getMessage();

        return ResponseEntity
                .status(status.getHttpStatus())
                .body(new BaseResponse<>(status, message, data));
    }
}
