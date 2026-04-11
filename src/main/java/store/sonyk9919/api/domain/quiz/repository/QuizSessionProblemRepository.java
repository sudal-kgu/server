package store.sonyk9919.api.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.quiz.entity.QuizSession;
import store.sonyk9919.api.domain.quiz.entity.QuizSessionProblem;

import java.util.List;

public interface QuizSessionProblemRepository extends JpaRepository<QuizSessionProblem, Long> {

    List<QuizSessionProblem> findAllBySession(QuizSession session);
}
