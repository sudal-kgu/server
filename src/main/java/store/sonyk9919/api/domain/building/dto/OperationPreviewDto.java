package store.sonyk9919.api.domain.building.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OperationPreviewDto {

    private int fuels;

    public static OperationPreviewDto from(int requiredFuel){
        return new OperationPreviewDto(requiredFuel);
    }
}
