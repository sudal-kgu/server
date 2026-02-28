package store.sonyk9919.api.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResultDto;
import store.sonyk9919.api.domain.analysis.exception.AnalysisStatus;
import store.sonyk9919.api.domain.analysis.repository.AnalysisResultRepository;
import store.sonyk9919.api.domain.analysis.repository.AnalysisResultSearchRepository;
import store.sonyk9919.api.domain.trash.entity.Trash;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.dto.PageResponseDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisResultFinder {

    private final AnalysisResultSearchRepository analysisResultSearchRepository;
    private final AnalysisResultRepository analysisResultRepository;

    public PageResponseDto<AnalysisResultDto> searchResults(String serial, Pageable pageable) {
        if (!analysisResultRepository.existsBySerial(serial)) throw new CustomException(AnalysisStatus.ANALYSIS_RESULT_NOT_FOUND);
        Page<Trash> page = analysisResultSearchRepository.findAllBy(serial, pageable);
        return PageResponseDto.from(page, AnalysisResultDto::from);
    }
}
