package store.sonyk9919.api.domain.quiz.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.sonyk9919.api.domain.quiz.entity.Quiz;
import store.sonyk9919.api.domain.quiz.exception.QuizStatus;
import store.sonyk9919.api.domain.quiz.repository.QuizRepository;
import store.sonyk9919.api.domain.taxonomy.entity.TrashCategory;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class QuizSelectServiceTest {

    @InjectMocks
    private QuizSelectService quizSelectService;

    @Mock
    private QuizRepository quizRepository;

    @Test
    void 요청한_개수만큼_퀴즈가_정확히_반환되어야_한다() {
        // Given
        TrashCategory category = mock(TrashCategory.class);
        int randomCount = ThreadLocalRandom.current().nextInt(3, 11);
        List<Long> mockQuizIds = LongStream.range(0, randomCount)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));

        List<Quiz> mockQuizzes = IntStream.range(0, randomCount)
                .mapToObj(i -> mock(Quiz.class))
                .toList();

        given(category.getId()).willReturn(1L);
        given(quizRepository.findAllIdByCategory(1L))
                .willReturn(mockQuizIds);
        given(quizRepository.findAllByIdIn(anyList()))
                .willReturn(mockQuizzes);

        // When
        List<Quiz> result = quizSelectService.select(category, randomCount);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(randomCount);
    }

    @Test
    void 요청한_개수와_퀴즈_개수가_일치하지_않으면_예외가_발생한다() {
        // Given
        TrashCategory category = mock(TrashCategory.class);
        int requestCount = ThreadLocalRandom.current().nextInt(3, 11);
        int returnedCount = requestCount - 1;
        List<Long> notEnoughQuizzes = LongStream.range(0, returnedCount)
                .boxed()
                .toList();

        given(quizRepository.findAllIdByCategory(1L))
                .willReturn(notEnoughQuizzes);
        given(category.getId()).willReturn(1L);

        // When & Then
        assertThatThrownBy(() -> quizSelectService.select(category, requestCount))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(QuizStatus.NOT_ENOUGH_QUIZ_NUMBER.getMessage());
    }
}