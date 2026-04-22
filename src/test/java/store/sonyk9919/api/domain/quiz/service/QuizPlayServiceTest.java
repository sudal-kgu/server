package store.sonyk9919.api.domain.quiz.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.quiz.dto.QuizProblemResponseDto;
import store.sonyk9919.api.domain.quiz.dto.QuizSessionResponseDto;
import store.sonyk9919.api.domain.quiz.entity.Quiz;
import store.sonyk9919.api.domain.quiz.entity.QuizOption;
import store.sonyk9919.api.domain.quiz.entity.QuizSession;
import store.sonyk9919.api.domain.quiz.entity.QuizSessionProblem;
import store.sonyk9919.api.domain.quiz.exception.QuizStatus;
import store.sonyk9919.api.domain.taxonomy.entity.TrashCategory;
import store.sonyk9919.api.domain.taxonomy.entity.TrashTaxonomy;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.domain.trash.service.TrashSearchService;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
class QuizPlayServiceTest {

    @InjectMocks
    private QuizPlayService quizPlayService;

    @Mock
    private QuizSelectService quizSelectService;
    @Mock
    private QuizSessionRegistryService quizSessionRegistryService;
    @Mock
    private QuizSessionProblemRegistryService quizSessionProblemRegistryService;
    @Mock
    private TrashSearchService trashSearchService;
    @Mock
    private MemberIslandRegistryService memberIslandRegistryService;

    @Test
    void 진행중인_세션이_없는_경우_새로운_세션과_문제를_반환한다() {
        // Given
        Long memberId = 1L;
        String serial = "TRASH-123";
        MemberIsland island = mock(MemberIsland.class);
        QuizSession newSession = mock(QuizSession.class);
        Trash trash = mock(Trash.class);
        TrashTaxonomy taxonomy = mock(TrashTaxonomy.class);
        TrashCategory category = mock(TrashCategory.class);

        given(memberIslandRegistryService.getIsland(memberId)).willReturn(island);
        given(quizSessionRegistryService.getActiveSession(island)).willReturn(Optional.empty());

        given(quizSessionRegistryService.create(island)).willReturn(newSession);
        given(trashSearchService.getTrashWithCategory(serial)).willReturn(trash);
        given(trash.getTaxonomy()).willReturn(taxonomy);
        given(taxonomy.getCategory()).willReturn(category);
        given(quizSelectService.select(eq(category), eq(3))).willReturn(List.of(mock(Quiz.class)));
        given(quizSessionProblemRegistryService.createAll(eq(newSession), anyList())).willReturn(List.of(mock(QuizSessionProblem.class)));

        // When
        QuizSessionResponseDto result = quizPlayService.startSession(memberId, serial);

        // Then
        assertThat(result).isNotNull();
        verify(quizSessionRegistryService).create(island);
        verify(quizSessionProblemRegistryService).createAll(eq(newSession), anyList());
    }

    @Test
    void 활성화된_세션이_없을_때_조회시_예외가_발생한다() {
        // Given
        Long memberId = 1L;
        MemberIsland island = mock(MemberIsland.class);

        given(memberIslandRegistryService.getIsland(memberId)).willReturn(island);
        given(quizSessionRegistryService.getActiveSession(island)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> quizPlayService.getActiveQuizSession(memberId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(QuizStatus.NOT_FOUND_QUIZ_SESSION.getMessage());
    }

    @Test
    void 퀴즈_답을_선택할_때_문제나_세션이_만료된_경우_예외가_발생한다() {
        // Given
        Long memberId = 1L, sessionId = 100L, problemId = 10L, choice = 2L;
        QuizSessionProblem problem = mock(QuizSessionProblem.class);
        QuizSession session = mock(QuizSession.class);

        given(quizSessionRegistryService.existsQuizSession(memberId, sessionId)).willReturn(true);
        given(quizSessionProblemRegistryService.getProblem(memberId, sessionId, problemId)).willReturn(problem);

        given(problem.isExpired()).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> quizPlayService.confirmQuizChoice(memberId, sessionId, problemId, choice))
                .isInstanceOf(CustomException.class);
        verify(problem, never()).confirmChoice(anyLong());
    }

    @Test
    void 권한이_있고_세션이_만료되지_않은_경우_답을_선택할_수_있다() {
        // Given
        Long memberId = 1L;
        Long sessionId = 100L;
        Long problemId = 10L;
        Long choiceId = 20L;

        QuizSessionProblem problem = mock(QuizSessionProblem.class);
        QuizSession session = mock(QuizSession.class);
        Quiz quiz = mock(Quiz.class);
        QuizOption option1 = mock(QuizOption.class);
        QuizOption option2 = mock(QuizOption.class);

        given(quizSessionRegistryService.existsQuizSession(memberId, sessionId)).willReturn(true);
        given(quizSessionProblemRegistryService.getProblem(memberId, sessionId, problemId)).willReturn(problem);

        given(problem.isExpired()).willReturn(false);
        given(problem.getSession()).willReturn(session);
        given(session.isExpired()).willReturn(false);

        given(session.getId()).willReturn(sessionId);
        given(problem.getId()).willReturn(problemId);
        given(problem.getExpiredAt()).willReturn(LocalDateTime.now().plusMinutes(10));

        given(problem.getQuiz()).willReturn(quiz);
        given(quiz.getDescription()).willReturn("다음 중 플라스틱으로 분리수거해야 할 것은?");
        given(quiz.getOptions()).willReturn(List.of(option1, option2));

        given(option1.getId()).willReturn(10L);
        given(option1.getDescription()).willReturn("페트병");
        given(option2.getId()).willReturn(20L);
        given(option2.getDescription()).willReturn("종이컵");
        given(problem.getDisplayOrder()).willReturn("10, 20");

        // When
        QuizProblemResponseDto result = quizPlayService.confirmQuizChoice(memberId, sessionId, problemId, choiceId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getSessionId()).isEqualTo(sessionId);
        assertThat(result.getDescription()).isEqualTo("다음 중 플라스틱으로 분리수거해야 할 것은?");
        assertThat(result.getChoices()).hasSize(2);
        assertThat(result.getChoices().getFirst().getId()).isEqualTo(10L);
        verify(problem).confirmChoice(choiceId);
    }
}