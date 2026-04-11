package store.sonyk9919.api.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.quiz.entity.QuizSession;

import java.time.LocalDateTime;
import java.util.Optional;

public interface QuizSessionRepository extends JpaRepository<QuizSession, Long> {
    boolean existsByIslandAndIsActiveAndExpiredAtAfter(MemberIsland island, boolean isActive, LocalDateTime now);
    Optional<QuizSession> findByIslandAndIsActiveAndExpiredAtAfter(MemberIsland island, boolean isActive, LocalDateTime now);
}
