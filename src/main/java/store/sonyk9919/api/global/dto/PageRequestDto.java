package store.sonyk9919.api.global.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageRequestDto {

    private Integer page;
    private Integer size;

    public Pageable toPageable() {
        int p = (page == null || page < 1) ? 0 : page - 1;
        int s = (size == null || size < 1) ? 10 : size;
        return PageRequest.of(p, s);
    }
}
