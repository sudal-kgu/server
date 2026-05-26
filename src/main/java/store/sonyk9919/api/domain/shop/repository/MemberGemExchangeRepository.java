package store.sonyk9919.api.domain.shop.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.member.entity.MemberAccount;
import store.sonyk9919.api.domain.shop.entity.GemItem;
import store.sonyk9919.api.domain.shop.entity.MemberGemExchange;

public interface MemberGemExchangeRepository extends JpaRepository<MemberGemExchange, Long> {

    long countByMemberAccountAndGemItemAndCreatedAtBetween(
            MemberAccount memberAccount,
            GemItem gemItem,
            LocalDateTime start,
            LocalDateTime end
    );
}
