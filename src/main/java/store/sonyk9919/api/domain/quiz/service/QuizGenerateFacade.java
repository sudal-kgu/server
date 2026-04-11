package store.sonyk9919.api.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.quiz.entity.Quiz;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.domain.trash.service.TrashSearchService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizGenerateFacade {

    private final QuizSelectService quizSelectService;
    private final TrashSearchService trashSearchService;

    @Transactional
    public void generate(String serial) {
        Trash trash = trashSearchService.getTrashWithCategory(serial);
        List<Quiz> select = quizSelectService.select(trash.getTaxonomy().getCategory(), 3);
    }
}
