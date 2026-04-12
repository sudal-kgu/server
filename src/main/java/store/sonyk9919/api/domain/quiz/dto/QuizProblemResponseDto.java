package store.sonyk9919.api.domain.quiz.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.quiz.entity.QuizOption;
import store.sonyk9919.api.domain.quiz.entity.QuizSessionProblem;
import store.sonyk9919.api.domain.quiz.exception.QuizStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizProblemResponseDto {

    private final Long sessionId;
    private final Long problemId;
    private final String description;
    private final List<QuizProblemChoiceDto> choices;
    private final LocalDateTime expiredAt;

    public static QuizProblemResponseDto from(QuizSessionProblem problem) {
        return new QuizProblemResponseDto(
                problem.getSession().getId(),
                problem.getId(),
                problem.getQuiz().getDescription(),
                orderChoices(problem.getQuiz().getOptions(), problem.getDisplayOrder()),
                problem.getExpiredAt()
        );
    }

    private static List<QuizProblemChoiceDto> orderChoices(List<QuizOption> options, String displayOrder) {
        Map<Long, QuizOption> quizOptionMap = options.stream()
                .collect(Collectors.toMap(QuizOption::getId, Function.identity()));
        String[] orders = displayOrder.split(",");
        return IntStream.range(0, orders.length)
                .mapToObj(index -> {
                    long optionId = Long.parseLong(orders[index].trim());
                    QuizOption option = quizOptionMap.get(optionId);
                    if (option == null) throw new CustomException(QuizStatus.NOT_FOUND_QUIZ_OPTION);
                    return QuizProblemChoiceDto.from(option, index + 1);
                })
                .toList();
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static class QuizProblemChoiceDto {
        private final Long id;
        private final String description;
        private final int order;

        public static QuizProblemChoiceDto from(QuizOption option, int order) {
            return new QuizProblemChoiceDto(option.getId(), option.getDescription(), order);
        }
    }
}
