package store.sonyk9919.api.domain.trash.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmRequestDto {

    private List<String> trashUuids;

    public List<String> getTrashUuids() {
        if(trashUuids == null) return List.of();
        return List.copyOf(trashUuids);
    }
}
