package store.sonyk9919.api.domain.trash.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TrashItemDto {
    private String trashId;
    private String filename;
    private String category;
    private String subcategory;

    public static TrashItemDto of(String trashId, String filename, String category, String subcategory) {
        return new TrashItemDto(trashId, filename, category, subcategory);
    }
}