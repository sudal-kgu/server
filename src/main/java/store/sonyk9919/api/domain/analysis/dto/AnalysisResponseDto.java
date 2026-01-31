package store.sonyk9919.api.domain.analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class AnalysisResponseDto {

    @JsonProperty("request_id")
    private String requestId;

    private Integer count;

    @JsonProperty("detected_items")
    private List<DetectedItem> detectedItems;

    @Getter
    @NoArgsConstructor
    @ToString
    public static class DetectedItem {
        private String label;
        private String material;
        private Confidence confidence;
        private String filename;

        @Getter
        @NoArgsConstructor
        @ToString
        public static class Confidence {
            private Double object;
            private Double material;
        }
    }
}