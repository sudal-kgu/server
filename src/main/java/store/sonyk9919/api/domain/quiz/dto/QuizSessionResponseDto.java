package store.sonyk9919.api.domain.quiz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.quiz.entity.QuizSession;
import store.sonyk9919.api.domain.quiz.entity.QuizSessionProblem;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizSessionResponseDto {
    private final Long sessionId;
    private final List<Long> problems;
    private final List<Long> unsolved;
    @JsonProperty("isNew") private final boolean isNew;

    public static QuizSessionResponseDto from(QuizSession session, List<QuizSessionProblem> problems, boolean isNew) {
        return new QuizSessionResponseDto(
                session.getId(),
                problems.stream().map(QuizSessionProblem::getId).sorted().toList(),
                problems.stream().filter(problem -> problem.getChoice() == null).map(QuizSessionProblem::getId).sorted().toList(),
                isNew
        );
    }
}
