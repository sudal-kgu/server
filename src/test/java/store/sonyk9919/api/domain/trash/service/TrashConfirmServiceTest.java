package store.sonyk9919.api.domain.trash.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.repository.AnalysisRequestRepository;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.service.IslandLevelService;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.taxonomy.entity.TrashTaxonomy;
import store.sonyk9919.api.domain.taxonomy.repository.TrashTaxonomyRepository;
import store.sonyk9919.api.domain.trash.dto.ConfirmResponseDto;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.domain.trash.repository.TrashRepository;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.config.QueryDslConfig;

@DataJpaTest
@Import({QueryDslConfig.class, TrashConfirmService.class})
@Transactional
class TrashConfirmServiceTest {

    @Autowired private TrashConfirmService trashConfirmService;
    @Autowired private TrashRepository trashRepository;
    @Autowired private AnalysisRequestRepository analysisRequestRepository;
    @Autowired private TrashTaxonomyRepository trashTaxonomyRepository;

    @MockitoBean private MemberIslandRegistryService memberIslandRegistryService;
    @MockitoBean private ResourceService resourceService;
    @MockitoBean private TrashConfirmRewardCalculator rewardCalculator;
    @MockitoBean private IslandLevelService islandLevelService;

    private Trash trash1;
    private Trash trash2;

    private final Long memberId = 1L;

    @BeforeEach
    void setup() {
        AnalysisRequest request = analysisRequestRepository.save(AnalysisRequest.createWithUUID());
        TrashTaxonomy taxonomy = trashTaxonomyRepository.findAll().get(0);
        trash1 = trashRepository.save(Trash.create(request, taxonomy, "a.jpg"));
        trash2 = trashRepository.save(Trash.create(request, taxonomy, "b.jpg"));

        MemberIsland mockIsland = mock(MemberIsland.class);
        given(memberIslandRegistryService.getIsland(memberId)).willReturn(mockIsland);
        given(rewardCalculator.calculateDisposalShell(mockIsland)).willReturn(10);
    }

    @Test
    @DisplayName("선택한 trash_uuid 목록을 선택하면 serial이 반환되고 Trash에 AnalysisResult 삽입")
    void confirmSuccess() {
        List<String> trashUuids = List.of(trash1.getTrashUuid(), trash2.getTrashUuid());

        ConfirmResponseDto response = trashConfirmService.confirm(memberId, trashUuids);
        List<Trash> confirmed = trashRepository.findAllByTrashUuidIn(trashUuids);

        confirmed.forEach(t ->
                assertThat(t.getAnalysisResult().getSerial()).isEqualTo(response.getSerial())
        );
    }

    @Test
    @DisplayName("존재하지 않는 trash_uuid 포함하면 confirm 실패")
    void confirmNotFound() {
        assertThatThrownBy(() ->
                trashConfirmService.confirm(memberId, List.of(trash1.getTrashUuid(), "fake1234"))
        ).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("이미 confirm된 Trash를 다시 시도하면 confirm 실패")
    void alreadyConfirmed() {
        trashConfirmService.confirm(memberId, List.of(trash1.getTrashUuid()));

        assertThatThrownBy(() ->
                trashConfirmService.confirm(memberId, List.of(trash1.getTrashUuid()))
        ).isInstanceOf(CustomException.class);
    }
}