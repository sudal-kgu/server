package store.sonyk9919.api.domain.trash.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.trash.dto.ConfirmRequestDto;
import store.sonyk9919.api.domain.trash.dto.ConfirmResponseDto;
import store.sonyk9919.api.domain.trash.service.TrashConfirmService;

@RestController
@RequestMapping("/api/v1/trashes")
@RequiredArgsConstructor
public class TrashConfirmController {

    private final TrashConfirmService trashConfirmService;

    @Operation(
            summary = "분석 객체 선택 확정",
            description = "클라이언트가 선택한 trash_uuid들을 받아 분석 결과(serial)로 묶어 저장합니다. " +
                    "반환된 serial로 페이징 조회가 가능합니다."
    )
    @PostMapping("/confirm")
    public ConfirmResponseDto confirmTrash(@RequestBody ConfirmRequestDto request) {
        return trashConfirmService.confirm(request.getTrashUuids());
    }
}