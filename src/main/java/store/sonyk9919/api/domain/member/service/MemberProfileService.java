package store.sonyk9919.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.member.entity.MemberProfile;
import store.sonyk9919.api.domain.member.repository.MemberProfileRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;

    @Transactional
    public MemberProfile createMemberProfile(String nickname) {
        MemberProfile profile = MemberProfile.from(nickname, 0L);
        memberProfileRepository.save(profile);
        return profile;
    }
}
