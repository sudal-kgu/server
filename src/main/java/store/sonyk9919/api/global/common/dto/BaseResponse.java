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
        this.message = (message != null) ? message : status.getMessage();
        this.data = data;
    }

    public static BaseResponse<Void> success (){
        return new BaseResponse<>(SuccessStatus.SUCCESS, null, null);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(SuccessStatus.SUCCESS, null, data);
    }

    public static ResponseEntity<BaseResponse<Void>> error(BaseResponseStatus status) {
        return buildError(status, null, null);
    }

    public static ResponseEntity<BaseResponse<Void>> error(BaseResponseStatus status, String customMessage) {
        return buildError(status, customMessage, null);
    }

    public static <T> ResponseEntity<BaseResponse<T>> error(BaseResponseStatus status, T data) {
        return buildError(status, null, data);
    }

    private static <T> ResponseEntity<BaseResponse<T>> buildError(BaseResponseStatus status, String customMessage, T data) {
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(new BaseResponse<>(status, customMessage, data));
    }
}
