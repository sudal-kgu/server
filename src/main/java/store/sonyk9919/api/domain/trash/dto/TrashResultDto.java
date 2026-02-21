package store.sonyk9919.api.domain.trash.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashResultDto {

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("trash_items")
    private List<TrashItemDto> trashItems;

    public boolean isEmptyItems(){
        return trashItems == null || trashItems.isEmpty();
    }

    public static TrashResultDto of(String requestId, List<TrashItemDto> items){
        return new TrashResultDto(requestId, items);
    }
}