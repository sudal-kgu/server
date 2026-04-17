package store.sonyk9919.api.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.sonyk9919.api.domain.quiz.entity.Quiz;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("SELECT q.id FROM Quiz q WHERE  q.category.id = :categoryId")
    List<Long> findAllIdByCategory(@Param("categoryId") Long categoryId);

    List<Quiz> findAllByIdIn(List<Long> ids);
}
