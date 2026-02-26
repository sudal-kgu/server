package store.sonyk9919.api.global.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import store.sonyk9919.api.global.common.dto.BaseResponse;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;
import store.sonyk9919.api.global.common.dto.ErrorStatus;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponse<Void>> handleCustomException(CustomException e, HttpServletRequest request) {
        BaseResponseStatus status = e.getStatus();

        log.warn("CustomException occurred at {}: [{}] {}",
                request.getRequestURI(), status.getCode(), e.getMessage());
        return BaseResponse.error(status, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> errors = getErrors(e);

        log.warn("Validation failed at {}: {}", request.getRequestURI(), errors);
        return BaseResponse.error(ErrorStatus.VALIDATION_FAIL, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Void>> handleJsonParsingExceptions(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("JSON parsing failed at {}: {}", request.getRequestURI(), e.getMessage());
        return BaseResponse.error(ErrorStatus.INVALID_REQUEST_BODY);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<BaseResponse<Void>> handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        log.warn("DataAccessException occurred at {}: {}", request.getRequestURI(), e.getMessage());
        return BaseResponse.error(ErrorStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("Method Not Allowed at {}: {}", request.getRequestURI(), e.getMessage());
        return BaseResponse.error(ErrorStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public void  handleAsyncRequestTimeout(AsyncRequestTimeoutException e) {
        log.warn("SSE connection timeout: {}", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleException(Exception e, HttpServletRequest request) {
        log.error("Unhandled Exception occurred at {}: ", request.getRequestURI(), e);
        return BaseResponse.error(ErrorStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, String> getErrors(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();

        for (FieldError error : bindingResult.getFieldErrors()) {
            String field = error.getField();
            String defaultMessage = error.getDefaultMessage();

            errors.put(field, defaultMessage);
        }
        return errors;
    }
}
