package store.sonyk9919.api.domain.island.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.dto.MemberIslandDto;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.repository.MemberIslandRepository;
import store.sonyk9919.api.domain.member.entity.MemberAccount;
import store.sonyk9919.api.domain.member.service.MemberAccountService;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberIslandRegistryService {

    private final MemberIslandRepository memberIslandRepository;
    private final MemberAccountService memberAccountService;

    @Transactional
    public MemberIsland create(Long memberAccountId, String nickname) {
        MemberAccount account = memberAccountService.getMemberAccount(memberAccountId);
        MemberIsland island = MemberIsland.create(nickname, account);
        memberIslandRepository.save(island);
        return island;
    }

    public MemberIsland getIsland(Long memberAccountId) {
        return memberIslandRepository.findByMemberAccountId(memberAccountId)
                .orElseThrow(() -> new CustomException(IslandStatus.NOT_FOUND_ISLAND));
    }

    public MemberIslandDto getIslandDto(Long memberAccountId) {
        MemberIsland island = memberIslandRepository.findByMemberAccountId(memberAccountId)
                .orElseThrow(() -> new CustomException(IslandStatus.NOT_FOUND_ISLAND));
        return MemberIslandDto.from(island);
    }
}
