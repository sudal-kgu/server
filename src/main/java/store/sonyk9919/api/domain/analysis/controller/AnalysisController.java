package store.sonyk9919.api.domain.analysis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResultDto;
import store.sonyk9919.api.domain.analysis.dto.RequestIdDto;
import store.sonyk9919.api.domain.analysis.service.AnalysisFacade;
import store.sonyk9919.api.domain.analysis.service.AnalysisResultFinder;
import store.sonyk9919.api.domain.analysis.service.AnalysisSseFacade;
import store.sonyk9919.api.global.common.dto.BaseResponse;
import store.sonyk9919.api.global.dto.PageRequestDto;
import store.sonyk9919.api.global.dto.PageResponseDto;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor
public class AnalysisController {
    private final AnalysisFacade analysisFacade;
    private final AnalysisSseFacade analysisSseFacade;
    private final AnalysisResultFinder analysisResultFinder;

    @Operation(
            summary = "이미지 분석 요청 등록",
            description = "사용자로부터 이미지를 받아 서버에 저장하고 비동기 분석 작업을 시작합니다. " +
                    "응답으로 즉시 requestId를 반환하고 실제 분석 결과는 SSE를 통해 수신해야 합니다."
    )
    @PostMapping(value = "/request", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RequestIdDto requestAnalysis(@RequestParam("image") MultipartFile image) {
        return RequestIdDto.from(analysisFacade.submitAnalysis(image));
    }

    @Operation(
            summary = "분석 응답 구독 (SSE)",
            description = "request_id로 분석 결과를 수신합니다. " +
                    "분석이 이미 완료된 경우 결과를 바로 반환합니다."
    )
    @GetMapping(value = "/subscribe/{requestId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribeAnalysis(@PathVariable("requestId") String requestId) {
        SseEmitter emitter = analysisSseFacade.subscribe(requestId);

        return ResponseEntity.ok()
                .header("X-Accel-Buffering", "no")
                .body(emitter);
    }

    @Operation(
            summary = "분석 결과 페이징 조회",
            description = "Serial로 분석된 쓰레기 목록을 가져옵니다. 존재하지 않는 Serial인 경우 404를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "Serial이 존재하지 않은 경우",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/result/{serial}")
    public PageResponseDto<AnalysisResultDto> getAnalysisResults(
            @PathVariable String serial,
            PageRequestDto pageRequestDto
    ) {
        return analysisResultFinder.searchResults(serial, pageRequestDto.toPageable());
    }
}