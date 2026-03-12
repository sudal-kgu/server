package store.sonyk9919.api.domain.trash.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.trash.dto.TrashDetailDto;
import store.sonyk9919.api.domain.trash.entity.TrashStatus;
import store.sonyk9919.api.domain.trash.repository.TrashSearchRepository;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.language.type.Language;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrashSearchService {

    private final TrashSearchRepository trashSearchRepository;

    public TrashDetailDto getTrash(String serial, Language language) {
        return trashSearchRepository.findBy(serial, language)
                .orElseThrow(() -> new CustomException(TrashStatus.NOT_FOUND_TRASH));
    }
}
