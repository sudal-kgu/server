package store.sonyk9919.api.domain.trash.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmRequestDto {

    @JsonProperty("trash_uuids")
    private List<String> trashUuids;

    public List<String> getTrashUuids() {
        if(trashUuids == null) return List.of();
        return List.copyOf(trashUuids);
    }
}
