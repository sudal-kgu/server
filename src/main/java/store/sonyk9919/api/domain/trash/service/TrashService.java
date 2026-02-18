package store.sonyk9919.api.domain.trash.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;
import store.sonyk9919.api.domain.taxonomy.exception.TaxonomyStatus;
import store.sonyk9919.api.domain.taxonomy.repository.TrashTaxonomyRepository;
import store.sonyk9919.api.domain.trash.dto.TrashCreateDto;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.domain.trash.repository.TrashRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
public class TrashService {
    private final TrashRepository trashRepository;
    private final TrashTaxonomyRepository taxonomyRepository;

    @Transactional
    public Trash saveTrash(TrashCreateDto trashCreateDto) {
        TrashTaxonomy taxonomy = taxonomyRepository
                .getTrashTaxonomiesByCategory_NameAndSubCategory_Alias(trashCreateDto.getCategory(), trashCreateDto.getSubCategory())
                .orElseThrow(() -> new CustomException(TaxonomyStatus.TAXONOMY_NOT_FOUND));

        Trash trash = Trash.create(
                trashCreateDto.getRequest(),
                trashCreateDto.getResult(),
                taxonomy,
                trashCreateDto.getFilename()
        );
        return trashRepository.save(trash);
    }
}