package store.sonyk9919.api.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum MemberStatus implements BaseResponseStatus {
    MEMBER_ACCOUNT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "MEMBER-001", "아이디 또는 비밀번호가 일치하지 않습니다."),
    MEMBER_ACCOUNT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "MEMBER-002", "잘 못 된 요청입니다."),
    REFRESH_TOKEN_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "REFRESH_TOKEN-001", "서버 내부 오류가 발생했습니다."),
    REFRESH_TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN-002", "만료된 요청 입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
