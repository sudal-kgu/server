package store.sonyk9919.api.domain.trash.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashCategory;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashSubCategory;
import store.sonyk9919.api.domain.taxonomy.entitiy.TrashTaxonomy;
import store.sonyk9919.api.domain.trash.entity.Trash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TrashItemDto {
    private String trashUuid;
    private String filename;
    private String category;
    private String subcategory;

    public static TrashItemDto from(Trash trash) {
        TrashTaxonomy taxonomy = trash.getTaxonomy();
        TrashCategory category = taxonomy.getCategory();
        TrashSubCategory subCategory = taxonomy.getSubCategory();

        return new TrashItemDto(
                trash.getTrashUuid(),
                trash.getFilename(),
                category.getName(),
                subCategory.getAlias()
        );
    }
}