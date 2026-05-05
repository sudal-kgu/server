package store.sonyk9919.api.domain.island.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import store.sonyk9919.api.domain.auth.dto.OAuthUserInfoDto;
import store.sonyk9919.api.domain.auth.entity.OAuthProviderType;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.repository.MemberIslandRepository;
import store.sonyk9919.api.domain.island.repository.MemberResourceRepository;
import store.sonyk9919.api.domain.member.entity.AccountRole;
import store.sonyk9919.api.domain.member.entity.MemberAccount;
import store.sonyk9919.api.domain.member.repository.MemberAccountRepository;
import store.sonyk9919.api.global.config.QueryDslConfig;

@DataJpaTest
@Import({QueryDslConfig.class, ResourceSetupService.class})
class ResourceSetupServiceTest {

    @Autowired private ResourceSetupService resourceSetupService;
    @Autowired private MemberAccountRepository memberAccountRepository;
    @Autowired private MemberIslandRepository memberIslandRepository;
    @Autowired private MemberResourceRepository memberResourceRepository;

    private MemberIsland savedIsland;

    @BeforeEach
    void setUp() {
        OAuthUserInfoDto userInfo = mock(OAuthUserInfoDto.class);
        given(userInfo.getId()).willReturn("test-" + System.nanoTime());
        MemberAccount account = memberAccountRepository.save(
                MemberAccount.from(userInfo, OAuthProviderType.KAKAO, AccountRole.USER)
        );
        savedIsland = memberIslandRepository.save(MemberIsland.create("테스트 섬", account));
    }

    @Test
    void setupResources_섬_생성_시_SHELL_GEM_FUEL_재화가_0으로_생성된다() {
        // when
        resourceSetupService.setupResources(savedIsland);

        // then
        List<MemberResource> resources = memberResourceRepository.findAllByIslandMemberAccountId(
                savedIsland.getMemberAccount().getId()
        );
        assertThat(resources).hasSize(3);
        assertThat(resources).allMatch(r -> r.getAmount() == 0);
    }

    @Test
    void setupResources_재화_타입이_모두_생성된다() {
        // when
        resourceSetupService.setupResources(savedIsland);

        // then
        List<MemberResource> resources = memberResourceRepository.findAllByIslandMemberAccountId(
                savedIsland.getMemberAccount().getId()
        );
        assertThat(resources)
                .extracting(MemberResource::getResourceType)
                .containsExactlyInAnyOrder(ResourceType.SHELL, ResourceType.GEM, ResourceType.FUEL);
    }
}
