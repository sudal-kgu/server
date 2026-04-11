package store.sonyk9919.api.domain.quiz.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizSessionResponseDto {
    private final Long sessionId;

    public static QuizSessionResponseDto from(Long sessionId) {
        return new QuizSessionResponseDto(sessionId);
    }
}
