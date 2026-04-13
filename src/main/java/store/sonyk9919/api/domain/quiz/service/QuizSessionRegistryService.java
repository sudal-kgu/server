package store.sonyk9919.api.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.quiz.entity.QuizSession;
import store.sonyk9919.api.domain.quiz.exception.QuizStatus;
import store.sonyk9919.api.domain.quiz.repository.QuizSessionRepository;
import store.sonyk9919.api.domain.quiz.repository.QuizSessionSearchRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizSessionRegistryService {

    private final QuizSessionRepository quizSessionRepository;
    private final QuizSessionSearchRepository quizSessionSearchRepository;

    @Transactional
    public QuizSession create(MemberIsland island) {
        if (quizSessionRepository.existsByIslandAndIsActiveAndExpiredAtAfter(island, true, LocalDateTime.now())) {
            throw new CustomException(QuizStatus.ALREADY_ACTIVATE);
        }
        QuizSession session = QuizSession.from(island);
        quizSessionRepository.save(session);
        return session;
    }

    public QuizSession getSession(Long memberAccountId, Long sessionId) {
        return quizSessionSearchRepository.findBy(memberAccountId, sessionId)
                .orElseThrow(() -> new CustomException(QuizStatus.NOT_FOUND_QUIZ_SESSION));
    }

    public Optional<QuizSession> getActiveSession(MemberIsland island) {
        return quizSessionRepository.findByIslandAndIsActiveAndExpiredAtAfter(island, true, LocalDateTime.now());
    }

    public boolean existsQuizSession(Long memberAccountId, Long sessionId) {
        return quizSessionSearchRepository.existsBy(memberAccountId, sessionId);
    }
}
