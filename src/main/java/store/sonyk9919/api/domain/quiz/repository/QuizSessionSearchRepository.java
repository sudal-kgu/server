package store.sonyk9919.api.domain.quiz.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static store.sonyk9919.api.domain.island.entity.QMemberIsland.*;
import static store.sonyk9919.api.domain.member.entity.QMemberAccount.*;
import static store.sonyk9919.api.domain.quiz.entity.QQuizSession.*;

@Repository
@RequiredArgsConstructor
public class QuizSessionSearchRepository {

    private final JPAQueryFactory factory;

    public boolean existsBy(Long memberAccountId, Long sessionId) {
        Integer found = factory.selectOne()
                .from(quizSession)
                .join(quizSession.island, memberIsland)
                .join(memberIsland.memberAccount, memberAccount)
                .where(
                        quizSession.id.eq(sessionId),
                        memberAccount.id.eq(memberAccountId)
                )
                .fetchFirst();
        return found != null;
    }
}
