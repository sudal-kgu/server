package store.sonyk9919.api.domain.analysis.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.global.file.serializer.ImageSerializer;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalysisResultDto {

    private final String uuid;
    @JsonSerialize(using = ImageSerializer.class) private final String image;
    private final String category;
    private final String subcategory;

    public static AnalysisResultDto from(Trash trash) {
        return new AnalysisResultDto(
                trash.getTrashUuid(),
                trash.getFilename(),
                trash.getTaxonomy().getCategory().getName(),
                trash.getTaxonomy().getSubCategory().getName()
        );
    }
}
