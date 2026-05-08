package store.sonyk9919.api.domain.trash.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.analysis.entity.AnalysisResult;
import store.sonyk9919.api.domain.analysis.repository.AnalysisResultRepository;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.trash.dto.ConfirmResponseDto;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.domain.trash.entity.TrashStatus;
import store.sonyk9919.api.domain.trash.repository.TrashRepository;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
public class TrashConfirmService {
    private final TrashRepository trashRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final MemberIslandRegistryService memberIslandRegistryService;
    private final ResourceService resourceService;
    private final TrashConfirmRewardCalculator rewardCalculator;

    @DistributedLock(key = "'island:' + #memberId + ':shell'")
    @Transactional
    public ConfirmResponseDto confirm(Long memberId, List<String> trashUuids) {
        List<Trash> trashes = findAndValidTrashes(trashUuids);

        AnalysisResult analysisResult = AnalysisResult.create();
        analysisResultRepository.save(analysisResult);

        trashes.forEach(trash -> trash.confirmResult(analysisResult));

        rewardShell(memberId, trashes);
        return ConfirmResponseDto.from(analysisResult.getSerial());
    }

    private List<Trash> findAndValidTrashes(List<String> trashUuids) {
        if (trashUuids.isEmpty()){
            throw new CustomException(TrashStatus.NOT_FOUND_TRASH);
        }

        List<Trash> trashes = trashRepository.findAllByTrashUuidIn(trashUuids);
        long uniqueCount = trashUuids.stream().distinct().count();

        if (trashes.size() != uniqueCount){
            throw new CustomException(TrashStatus.NOT_FOUND_TRASH);
        }
        if (trashes.stream().anyMatch(Trash::isConfirmed)) {
            throw new CustomException(TrashStatus.ALREADY_CONFIRMED_TRASH);
        }
        return trashes;
    }

    private void rewardShell(Long memberAccountId, List<Trash> trashes) {
        MemberIsland island = memberIslandRegistryService.getIsland(memberAccountId);
        int earnedShell = rewardCalculator.calculateDisposalShell(island);

        resourceService.add(
                island, ResourceType.SHELL,
                (long) earnedShell * trashes.size()
        );
    }
}