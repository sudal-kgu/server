package store.sonyk9919.api.domain.member.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.auth.entity.OAuthProviderType;
import store.sonyk9919.api.domain.member.entity.MemberAccount;

import java.util.Optional;

public interface MemberAccountRepository extends JpaRepository<MemberAccount, Long> {

    Optional<MemberAccount> findByProviderIdAndType(String providerId, OAuthProviderType type);

    @EntityGraph(attributePaths = { "profile" })
    Optional<MemberAccount> findWithProfileById(Long id);
}
