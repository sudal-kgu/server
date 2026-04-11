package store.sonyk9919.api.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.quiz.dto.QuizCreateRequestDto;
import store.sonyk9919.api.domain.quiz.dto.QuizSessionResponseDto;
import store.sonyk9919.api.domain.quiz.entity.Quiz;
import store.sonyk9919.api.domain.quiz.entity.QuizSession;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.domain.trash.service.TrashSearchService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizGenerateFacade {

    private final QuizSelectService quizSelectService;
    private final QuizSessionRegistryService quizSessionRegistryService;
    private final QuizSessionProblemRegistryService quizSessionProblemRegistryService;
    private final TrashSearchService trashSearchService;
    private final MemberIslandRegistryService memberIslandRegistryService;

    @Transactional
    public QuizSessionResponseDto generate(Long memberAccountId, String serial) {
        MemberIsland island = memberIslandRegistryService.getIslandWithWriteLock(memberAccountId);

        return quizSessionRegistryService.getActiveSession(island)
                .map(session -> {
                    quizSessionProblemRegistryService.getProblems(session);
                    return QuizSessionResponseDto.from(session.getId());
                })
                .orElseGet(() -> {
                    QuizSession session = quizSessionRegistryService.create(island);
                    List<Quiz> quizzes = getQuizzes(serial);
                    quizSessionProblemRegistryService.createAll(session, quizzes);
                    return QuizSessionResponseDto.from(session.getId());
                });
    }

    private List<Quiz> getQuizzes(String serial) {
        Trash trash = trashSearchService.getTrashWithCategory(serial);
        return quizSelectService.select(trash.getTaxonomy().getCategory(), 3);
    }
}
