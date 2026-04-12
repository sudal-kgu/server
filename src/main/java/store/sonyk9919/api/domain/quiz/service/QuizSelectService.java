package store.sonyk9919.api.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.domain.quiz.entity.Quiz;
import store.sonyk9919.api.domain.quiz.exception.QuizStatus;
import store.sonyk9919.api.domain.quiz.repository.QuizRepository;
import store.sonyk9919.api.domain.taxonomy.entity.TrashCategory;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizSelectService {

    private final QuizRepository quizRepository;

    public List<Quiz> select(TrashCategory category, int number) {
        List<Quiz> quizzes = quizRepository.findAllByCategory(category.getId(), number);
        if (quizzes.size() != number) {
            throw new CustomException(QuizStatus.NOT_ENOUGH_QUIZ_NUMBER);
        }
        return quizzes;
    }
}
