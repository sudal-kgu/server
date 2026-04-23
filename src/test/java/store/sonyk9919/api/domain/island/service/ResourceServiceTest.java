package store.sonyk9919.api.domain.island.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import store.sonyk9919.api.domain.auth.dto.OAuthUserInfoDto;
import store.sonyk9919.api.domain.auth.entity.OAuthProviderType;
import store.sonyk9919.api.domain.island.dto.ResourceBalanceResponse;
import store.sonyk9919.api.domain.island.dto.ResourceChange;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.repository.MemberIslandRepository;
import store.sonyk9919.api.domain.island.repository.MemberResourceRepository;
import store.sonyk9919.api.domain.member.entity.AccountRole;
import store.sonyk9919.api.domain.member.entity.MemberAccount;
import store.sonyk9919.api.domain.member.repository.MemberAccountRepository;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.config.QueryDslConfig;

@DataJpaTest
@Import({QueryDslConfig.class, ResourceService.class})
class ResourceServiceTest {

    @Autowired private ResourceService resourceService;
    @Autowired private MemberAccountRepository memberAccountRepository;
    @Autowired private MemberIslandRepository memberIslandRepository;
    @Autowired private MemberResourceRepository memberResourceRepository;
    private MemberAccount savedAccount;
    private MemberIsland savedIsland;

    @AfterEach
    void tearDown() {
        memberResourceRepository.deleteAll();
        memberIslandRepository.deleteAll();
        memberAccountRepository.deleteAll();
    }

    private void setup(long shell, long gem, long fuel) {
        OAuthUserInfoDto userInfo = mock(OAuthUserInfoDto.class);
        given(userInfo.getId()).willReturn("test-" + System.nanoTime());
        savedAccount = memberAccountRepository.save(
                MemberAccount.from(userInfo, OAuthProviderType.KAKAO, AccountRole.USER)
        );
        savedIsland = memberIslandRepository.save(MemberIsland.create("테스트 섬", savedAccount));
        memberResourceRepository.saveAll(List.of(
                initResource(ResourceType.SHELL, savedIsland, shell),
                initResource(ResourceType.GEM, savedIsland, gem),
                initResource(ResourceType.FUEL, savedIsland, fuel)
        ));
    }

    private MemberResource initResource(ResourceType type, MemberIsland island, long amount) {
        MemberResource resource = MemberResource.create(type, island);
        if (amount > 0) resource.addAmount(amount);
        return resource;
    }

    @Test
    void add_재화_획득_시_금액이_증가한다() {
        setup(100, 0, 0);

        resourceService.add(savedIsland, ResourceType.SHELL, 50);

        MemberResource shell = memberResourceRepository
                .findWithLockByIslandAndResourceType(savedIsland, ResourceType.SHELL).get();
        assertThat(shell.getAmount()).isEqualTo(150);
    }

    @Test
    void subtract_재화_차감_시_금액이_감소한다() {
        setup(100, 0, 0);

        resourceService.subtract(savedIsland, ResourceType.SHELL, 30);

        MemberResource shell = memberResourceRepository
                .findWithLockByIslandAndResourceType(savedIsland, ResourceType.SHELL).get();
        assertThat(shell.getAmount()).isEqualTo(70);
    }

    @Test
    void subtract_잔액_부족_시_예외가_발생한다() {
        setup(100, 0, 0);

        assertThatThrownBy(() -> resourceService.subtract(savedIsland, ResourceType.SHELL, 200))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(IslandStatus.INSUFFICIENT_AMOUNT.getMessage());
    }

    @Test
    void getBalance_회원의_전체_재화_잔액을_반환한다() {
        setup(100, 50, 30);

        ResourceBalanceResponse balance = resourceService.getBalance(savedAccount.getId());

        assertThat(balance.getShell()).isEqualTo(100);
        assertThat(balance.getGem()).isEqualTo(50);
        assertThat(balance.getFuel()).isEqualTo(30);
    }

    @Test
    void applyMultiple_여러_재화를_한번에_변경한다() {
        setup(100, 0, 50);

        resourceService.applyMultiple(savedIsland, List.of(
                ResourceChange.add(ResourceType.SHELL, 10),
                ResourceChange.subtract(ResourceType.FUEL, 20)
        ));

        MemberResource shell = memberResourceRepository
                .findWithLockByIslandAndResourceType(savedIsland, ResourceType.SHELL).get();
        MemberResource fuel = memberResourceRepository
                .findWithLockByIslandAndResourceType(savedIsland, ResourceType.FUEL).get();
        assertThat(shell.getAmount()).isEqualTo(110);
        assertThat(fuel.getAmount()).isEqualTo(30);
    }

    @Test
    void applyMultiple_전달_순서와_무관하게_항상_동일한_순서로_락을_획득한다() {
        MemberResourceRepository mockRepo = mock(MemberResourceRepository.class);
        ResourceService localService = new ResourceService(mockRepo);

        MemberIsland island = mock(MemberIsland.class);
        MemberResource shellResource = mock(MemberResource.class);
        MemberResource fuelResource = mock(MemberResource.class);
        given(mockRepo.findWithLockByIslandAndResourceType(island, ResourceType.SHELL))
                .willReturn(java.util.Optional.of(shellResource));
        given(mockRepo.findWithLockByIslandAndResourceType(island, ResourceType.FUEL))
                .willReturn(java.util.Optional.of(fuelResource));

        localService.applyMultiple(island, List.of(
                ResourceChange.subtract(ResourceType.FUEL, 5),
                ResourceChange.add(ResourceType.SHELL, 10)
        ));

        InOrder inOrder = inOrder(mockRepo);
        inOrder.verify(mockRepo).findWithLockByIslandAndResourceType(island, ResourceType.SHELL);
        inOrder.verify(mockRepo).findWithLockByIslandAndResourceType(island, ResourceType.FUEL);
    }
}
