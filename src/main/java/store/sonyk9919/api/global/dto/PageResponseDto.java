package store.sonyk9919.api.global.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PageResponseDto<R> {

    private final long totalPage;
    private final int currentPage;
    private final List<R> content;

    public static <T, R> PageResponseDto<R> from(Page<T> page, Function<T, R> transformer) {
        return new PageResponseDto<>(
                page.getTotalPages(),
                page.getNumber() + 1,
                page.getContent()
                        .stream()
                        .map(transformer)
                        .toList()
        );
    }
}
