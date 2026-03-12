package store.sonyk9919.api.domain.trash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.sonyk9919.api.domain.trash.dto.TrashDetailDto;
import store.sonyk9919.api.domain.trash.service.TrashSearchService;
import store.sonyk9919.api.global.language.type.Language;

@RestController
@RequestMapping("/api/v1/trashes")
@RequiredArgsConstructor
public class TrashDisposalController {

    private final TrashSearchService trashSearchService;

    @Operation(
            summary = "쓰레기 상세 및 배출 방법 조회",
            description = "일련번호를 통해 특정 쓰레기의 상세 정보와 카테고리별 배출 방법을 조회합니다. Accept-Language 헤더에 따라 다국어 데이터를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 쓰레기 번호")
    })
    @GetMapping("/{serial}")
    @ResponseStatus(HttpStatus.OK)
    public TrashDetailDto getTrashDisposal(
            @Parameter(description = "쓰레기 고유 일련번호", example = "113")
            @PathVariable String serial,
            @Parameter(
                    name = "Accept-Language",
                    description = "사용자 선호 언어 (ko: 한국어, en: 영어)",
                    in = ParameterIn.HEADER,
                    schema = @Schema(type = "string", allowableValues = {"ko", "en"}, defaultValue = "ko")
            )
            @RequestHeader(name = "Accept-Language", required = false) String acceptLanguage
    ) {
        return trashSearchService.getTrash(serial, Language.from(acceptLanguage));
    }
}
