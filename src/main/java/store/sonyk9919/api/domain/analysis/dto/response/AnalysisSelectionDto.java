package store.sonyk9919.api.domain.analysis.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnalysisSelectionDto {
    private String requestId;
    private List<MappedItem> mappedItems;

    @Getter
    @AllArgsConstructor
    public static class MappedItem {
        private Long trashId;
        private String filename;
        private String category;
        private String subcategory;
    }

    public static AnalysisSelectionDto of(String requestId, List<MappedItem> mappedItems){
        return new AnalysisSelectionDto(requestId, mappedItems);
    }
}