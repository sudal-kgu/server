package store.sonyk9919.api.domain.analysis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.global.common.dto.ErrorStatus;

@Getter
@NoArgsConstructor
public class AnalysisCacheDto {
    private String type;
    private AnalysisResponseDto successData;
    private ErrorStatus errorStatus;

    public static AnalysisCacheDto success(AnalysisResponseDto data) {
        AnalysisCacheDto dto = new AnalysisCacheDto();
        dto.type = "SUCCESS";
        dto.successData = data;
        return dto;
    }

    public static AnalysisCacheDto error(ErrorStatus status) {
        AnalysisCacheDto dto = new AnalysisCacheDto();
        dto.type = "ERROR";
        dto.errorStatus = status;
        return dto;
    }

    public boolean isSuccess(){
        return type.equals("SUCCESS");
    }
}
