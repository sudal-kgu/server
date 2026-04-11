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
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
