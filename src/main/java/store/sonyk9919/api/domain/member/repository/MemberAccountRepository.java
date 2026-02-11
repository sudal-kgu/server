package store.sonyk9919.api.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.member.entitiy.MemberAccount;

import java.util.Optional;

public interface MemberAccountRepository extends JpaRepository<MemberAccount, Long> {

    Optional<MemberAccount> findByName(String username);
}
