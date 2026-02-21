package store.sonyk9919.api.domain.analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class AnalysisResponseDto {

    @JsonProperty("request_id")
    private String requestId;

    private Integer count;

    @JsonProperty("detected_items")
    private List<DetectedItem> detectedItems;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString
    public static class DetectedItem {
        private String category;
        private String subcategory;
        private Confidence confidence;
        private String filename;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @ToString
        public static class Confidence {
            private Double object;
            private Double material;
        }
    }
}