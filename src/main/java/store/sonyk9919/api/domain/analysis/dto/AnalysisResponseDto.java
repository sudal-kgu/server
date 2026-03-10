package store.sonyk9919.api.domain.analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnalysisResponseDto {

    @JsonProperty("request_id")
    private String requestId;

    private Integer count;

    @JsonProperty("detected_items")
    private List<DetectedItemDto> detectedItems;

    public static AnalysisResponseDto of(String requestId, List<DetectedItemDto> detectedItems) {
        return new AnalysisResponseDto(requestId, detectedItems.size(), detectedItems);
    }
}