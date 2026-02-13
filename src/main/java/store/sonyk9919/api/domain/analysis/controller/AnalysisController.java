package store.sonyk9919.api.domain.analysis.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.sonyk9919.api.domain.analysis.manager.AnalysisSubscriptionManager;
import store.sonyk9919.api.domain.analysis.service.AnalysisService;
import store.sonyk9919.api.global.common.dto.BaseResponse;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor
public class AnalysisController {
    private final AnalysisService analysisService;
    private final AnalysisSubscriptionManager subscriptionManager;

    @Operation(
            summary = "이미지 분석 요청 등록",
            description = "사용자로부터 이미지를 받아 서버에 저장하고 비동기 분석 작업을 시작합니다. " +
                    "응답으로 즉시 requestId를 반환하고 실제 분석 결과는 SSE를 통해 수신해야 합니다."
    )
    @PostMapping(value = "/request", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<String>> requestAnalysis(@RequestParam("image") MultipartFile image) {
        return BaseResponse.success(analysisService.submitAnalysis(image));
    }

    @Operation(
            summary = "분석 응답 구독 (SSE)",
            description = "request_id로 분석 결과를 수신합니다."
    )
    @GetMapping(value = "/subscribe/{requestId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribeAnalysis(@PathVariable("requestId") String requestId) {
        SseEmitter emitter = subscriptionManager.subscribe(requestId);

        return ResponseEntity.ok()
                .header("X-Accel-Buffering", "no")
                .body(emitter);
    }

    @Operation(
            summary = "분석 결과 캐시 확인",
            description = "SSE 연결 후 이미 완료된 분석 결과가 있는지 확인하고 있으면 전송합니다."
    )
    @GetMapping("/check/{requestId}")
    public ResponseEntity<BaseResponse<Void>> checkCacheResult(@PathVariable String requestId) {
        subscriptionManager.checkAndSendCacheResult(requestId);
        return BaseResponse.success();
    }
}