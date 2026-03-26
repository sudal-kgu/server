package store.sonyk9919.api.domain.trash.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.global.file.serializer.ImageSerializer;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TrashItemDto {
    private String trashUuid;
    @JsonSerialize(using = ImageSerializer.class) private String filename;
    private String category;
    private String subcategory;

    public static TrashItemDto from(Trash trash) {
        return new TrashItemDto(
                trash.getTrashUuid(),
                trash.getCropImagePath(),
                trash.getCategoryName(),
                trash.getSubCategoryName()
        );
    }
}