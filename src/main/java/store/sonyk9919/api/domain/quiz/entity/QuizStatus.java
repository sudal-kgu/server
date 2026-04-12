package store.sonyk9919.api.domain.quiz.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum QuizStatus implements BaseResponseStatus {
    ALREADY_ACTIVATE(HttpStatus.BAD_REQUEST, "QUIZ-001", "이미 풀고 있는 퀴즈가 존재합니다."),
    NOT_ENOUGH_QUIZ_NUMBER(HttpStatus.INTERNAL_SERVER_ERROR, "QUIZ-002", "서버 내부 오류가 발생했습니다."),
    NO_ANSWER_IN_OPTIONS(HttpStatus.INTERNAL_SERVER_ERROR, "QUIZ-003", "서버 내부 오류가 발생했습니다."),
    NOT_FOUND_QUIZ_OPTION(HttpStatus.INTERNAL_SERVER_ERROR, "QUIZ-OPTION-001", "서버 내부 오류가 발생했습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "QUIZ-SESSION-001", "접근 권한이 없습니다."),
    EXPIRED_QUIZ_SESSION(HttpStatus.GONE, "QUIZ-SESSION-002", "퀴즈 세션이 만료되었습니다."),
    NOT_FOUND_QUIZ_SESSION(HttpStatus.NOT_FOUND, "QUIZ-SESSION-003", "활성화된 퀴즈 세션이 없습니다."),
    NOT_FOUND_QUIZ_SESSION_PROBLEM(HttpStatus.NOT_FOUND, "QUIZ-SESSION-PROBLEM-001", "퀴즈를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
