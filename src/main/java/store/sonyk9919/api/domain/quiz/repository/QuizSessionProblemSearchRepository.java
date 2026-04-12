package store.sonyk9919.api.domain.quiz.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import store.sonyk9919.api.domain.island.entity.QMemberIsland;
import store.sonyk9919.api.domain.member.entity.QMemberAccount;
import store.sonyk9919.api.domain.quiz.entity.QQuiz;
import store.sonyk9919.api.domain.quiz.entity.QQuizSession;
import store.sonyk9919.api.domain.quiz.entity.QQuizSessionProblem;
import store.sonyk9919.api.domain.quiz.entity.QuizSessionProblem;

import java.util.Optional;

import static store.sonyk9919.api.domain.island.entity.QMemberIsland.*;
import static store.sonyk9919.api.domain.member.entity.QMemberAccount.*;
import static store.sonyk9919.api.domain.quiz.entity.QQuiz.*;
import static store.sonyk9919.api.domain.quiz.entity.QQuizSession.*;
import static store.sonyk9919.api.domain.quiz.entity.QQuizSessionProblem.*;

@Repository
@RequiredArgsConstructor
public class QuizSessionProblemSearchRepository {

    private final JPAQueryFactory factory;

    public Optional<QuizSessionProblem> findBy(Long memberAccountId, Long sessionId, Long problemId) {
        QuizSessionProblem problem = factory.selectFrom(quizSessionProblem)
                .join(quizSessionProblem.session, quizSession).fetchJoin()
                .join(quizSession.island, memberIsland)
                .join(memberIsland.memberAccount, memberAccount)
                .join(quizSessionProblem.quiz, quiz).fetchJoin()
                .where(isOwner(memberAccountId, sessionId, problemId))
                .fetchOne();

        return Optional.ofNullable(problem);
    }

    private BooleanExpression isOwner(Long memberAccountId, Long sessionId, Long problemId) {
        return memberAccount.id.eq(memberAccountId)
                .and(quizSession.id.eq(sessionId))
                .and(quizSessionProblem.id.eq(problemId));
    }
}
