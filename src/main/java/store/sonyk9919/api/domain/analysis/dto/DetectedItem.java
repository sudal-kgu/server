package store.sonyk9919.api.domain.analysis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class DetectedItem {
    private String category;
    private String subcategory;
    private Confidence confidence;
    private String filename;

    @Getter
    @NoArgsConstructor
    @ToString
    public static class Confidence {
        private Double object;
        private Double category;
    }
}
