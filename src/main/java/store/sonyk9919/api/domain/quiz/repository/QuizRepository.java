package store.sonyk9919.api.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.sonyk9919.api.domain.quiz.entity.Quiz;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("SELECT q FROM Quiz q WHERE q.category.id = :categoryId AND q.isActive = TRUE ORDER BY RAND() LIMIT :limit")
    List<Quiz> findAllByCategory(@Param("categoryId") Long categoryId, @Param("limit") int limit);
}
