package store.sonyk9919.api.domain.quiz.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.quiz.entity.QuizSession;
import store.sonyk9919.api.domain.quiz.exception.QuizStatus;
import store.sonyk9919.api.domain.quiz.repository.QuizSessionRepository;
import store.sonyk9919.api.domain.quiz.repository.QuizSessionSearchRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuizSessionRegistryServiceTest {

    @InjectMocks
    private QuizSessionRegistryService quizSessionRegistryService;

    @Mock
    private QuizSessionRepository quizSessionRepository;

    @Mock
    private QuizSessionSearchRepository quizSessionSearchRepository;

    @Test
    void 활성화된_세션이_없는_경우_새로운_세션을_반환한다() {
        // Given
        MemberIsland island = mock(MemberIsland.class);

        given(quizSessionRepository.existsByIslandAndIsActiveAndExpiredAtAfter(
                eq(island), eq(true), any(LocalDateTime.class)
        )).willReturn(false);

        // When
        QuizSession newSession = quizSessionRegistryService.create(island);

        // Then
        assertThat(newSession).isNotNull();
        verify(quizSessionRepository).save(newSession);
    }

    @Test
    void 활성화된_세션이_존재하면_세션_생성_시_예외가_발생한다() {
        // Given
        MemberIsland island = mock(MemberIsland.class);

        given(quizSessionRepository.existsByIslandAndIsActiveAndExpiredAtAfter(
                eq(island), eq(true), any(LocalDateTime.class)
        )).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> quizSessionRegistryService.create(island))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(QuizStatus.ALREADY_ACTIVATE.getMessage());
    }

    @Test
    void 세션ID로_조회_시_세션이_있으면_정상_반환한다() {
        // Given
        Long memberId = 1L;
        Long sessionId = 100L;
        QuizSession session = mock(QuizSession.class);

        given(quizSessionSearchRepository.findBy(memberId, sessionId)).willReturn(Optional.of(session));

        // When
        QuizSession result = quizSessionRegistryService.getSession(memberId, sessionId);

        // Then
        assertThat(result).isEqualTo(session);
    }

    @Test
    void 세션ID로_조회시_세션이_없으면_예외가_발생한다() {
        // Given
        Long memberId = 1L;
        Long sessionId = 100L;

        given(quizSessionSearchRepository.findBy(memberId, sessionId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> quizSessionRegistryService.getSession(memberId, sessionId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(QuizStatus.NOT_FOUND_QUIZ_SESSION.getMessage());
    }
}