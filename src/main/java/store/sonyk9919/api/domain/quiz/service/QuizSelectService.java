package store.sonyk9919.api.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.quiz.entity.Quiz;
import store.sonyk9919.api.domain.quiz.exception.QuizStatus;
import store.sonyk9919.api.domain.quiz.repository.QuizRepository;
import store.sonyk9919.api.domain.taxonomy.entity.TrashCategory;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizSelectService {

    private final QuizRepository quizRepository;

    public List<Quiz> select(TrashCategory category, int number) {
        List<Long> quizzes = quizRepository.findAllIdByCategory(category.getId());
        if (quizzes.size() < number) {
            throw new CustomException(QuizStatus.NOT_ENOUGH_QUIZ_NUMBER);
        }
        Collections.shuffle(quizzes);
        List<Long> ids = quizzes.subList(0, number);
        return quizRepository.findAllByIdIn(ids);
    }
}
