package store.sonyk9919.api.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.member.entitiy.MemberProfile;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
}
