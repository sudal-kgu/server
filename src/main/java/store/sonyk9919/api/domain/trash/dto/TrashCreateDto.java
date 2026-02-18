package store.sonyk9919.api.domain.trash.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.analysis.entity.AnalysisRequest;
import store.sonyk9919.api.domain.analysis.entity.AnalysisResult;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TrashCreateDto {
    private AnalysisRequest request;
    private AnalysisResult result;
    private String category;
    private String subCategory;
    private String filename;

    public static TrashCreateDto create(
            AnalysisRequest request,
            AnalysisResult result,
            String category,
            String subCategory,
            String filename)
    {
        return new TrashCreateDto(request, result, category, subCategory, filename);
    }
}
