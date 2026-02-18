package store.sonyk9919.api.domain.analysis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import store.sonyk9919.api.domain.analysis.dto.DetectedItem;

@Getter
@NoArgsConstructor
@ToString
public class AnalysisResponseDto {

    @JsonProperty("request_id")
    private String requestId;

    private Integer count;

    @JsonProperty("detected_items")
    private List<DetectedItem> detectedItems;
}