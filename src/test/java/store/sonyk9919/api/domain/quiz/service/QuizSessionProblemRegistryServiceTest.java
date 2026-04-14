package store.sonyk9919.api.domain.quiz.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import store.sonyk9919.api.domain.quiz.entity.Quiz;
import store.sonyk9919.api.domain.quiz.entity.QuizOption;
import store.sonyk9919.api.domain.quiz.entity.QuizSession;
import store.sonyk9919.api.domain.quiz.entity.QuizSessionProblem;
import store.sonyk9919.api.domain.quiz.repository.QuizSessionProblemRepository;
import store.sonyk9919.api.domain.quiz.repository.QuizSessionProblemSearchRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class QuizSessionProblemRegistryServiceTest {

    @InjectMocks private QuizSessionProblemRegistryService quizSessionProblemRegistryService;

    @Mock private QuizSessionProblemRepository quizSessionProblemRepository;
    @Mock private QuizSessionProblemSearchRepository quizSessionProblemSearchRepository;


    @Test
    void 퀴즈_세션의_문제를_생성할_때_선택지를_섞고_정답을_정확히_찾아야_한다() {
        // Given
        QuizSession session = mock(QuizSession.class);
        Quiz quiz = mock(Quiz.class);

        QuizOption option1 = mock(QuizOption.class);
        given(option1.getId()).willReturn(10L);
        given(option1.isAnswer()).willReturn(false);

        QuizOption option2 = mock(QuizOption.class);
        given(option2.getId()).willReturn(20L);
        given(option2.isAnswer()).willReturn(true);

        QuizOption option3 = mock(QuizOption.class);
        given(option3.getId()).willReturn(30L);
        given(option3.isAnswer()).willReturn(false);

        given(quiz.getOptions()).willReturn(List.of(option1, option2, option3));

        // When
        List<QuizSessionProblem> problems = quizSessionProblemRegistryService.createAll(session, List.of(quiz));

        // Then
        assertThat(problems.size()).isEqualTo(1);

        QuizSessionProblem problem = problems.getFirst();
        assertThat(problem).isNotNull();
        assertThat(problem.getAnswer()).isEqualTo(20L);
        assertThat(problem.getDisplayOrder()).contains("10", "20", "30");
        verify(quizSessionProblemRepository).saveAll(anyList());
    }

    @Test
    void 퀴즈의_정답이_없는_경우_예외가_발생한다() {
        // Given
        QuizSession session = mock(QuizSession.class);
        Quiz quiz = mock(Quiz.class);

        QuizOption option1 = mock(QuizOption.class);
        given(option1.getId()).willReturn(10L);
        given(option1.isAnswer()).willReturn(false);

        QuizOption option2 = mock(QuizOption.class);
        given(option2.getId()).willReturn(20L);
        given(option2.isAnswer()).willReturn(false);

        QuizOption option3 = mock(QuizOption.class);
        given(option3.getId()).willReturn(30L);
        given(option3.isAnswer()).willReturn(false);

        given(quiz.getOptions()).willReturn(List.of(option1, option2, option3));

        // When & Then
        Assertions.assertThrows(CustomException.class,
                () -> quizSessionProblemRegistryService.createAll(session, List.of(quiz)));
    }

    @Test
    void 퀴즈_세션이_만료되지_않은_경우_정상_조회_가능하다() {
        // Given
        Long memberAccountId = 1L;
        Long sessionId = 1L;
        Long problemId = 1L;
        QuizSession session = mock(QuizSession.class);
        QuizSessionProblem mockProblem = mock(QuizSessionProblem.class);

        given(quizSessionProblemSearchRepository.findBy(memberAccountId, sessionId, problemId))
                .willReturn(Optional.of(mockProblem));
        given(mockProblem.getSession()).willReturn(session);
        given(session.isExpired()).willReturn(false);

        // When
        QuizSessionProblem problem = quizSessionProblemRegistryService.getProblem(memberAccountId, sessionId, problemId);

        // Then
        assertThat(problem).isEqualTo(mockProblem);
    }

    @Test
    void 퀴즈_세션이_만료된_경우_문제를_조회할_수_없다() {
        // Given
        Long memberAccountId = 1L;
        Long sessionId = 1L;
        Long problemId = 1L;
        QuizSession session = mock(QuizSession.class);
        QuizSessionProblem mockProblem = mock(QuizSessionProblem.class);

        given(quizSessionProblemSearchRepository.findBy(memberAccountId, sessionId, problemId))
                .willReturn(Optional.of(mockProblem));
        given(mockProblem.getSession()).willReturn(session);
        given(session.isExpired()).willReturn(true);

        // When & Then
        assertThrows(CustomException.class,
                () -> quizSessionProblemRegistryService.getProblem(memberAccountId, sessionId, problemId));
    }
}