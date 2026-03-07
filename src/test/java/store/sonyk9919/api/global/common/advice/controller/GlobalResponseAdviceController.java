package store.sonyk9919.api.global.common.advice.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.global.common.dto.BaseResponse;

@RestController
@RequestMapping("/advice")
public class GlobalResponseAdviceController {

    @Getter
    @AllArgsConstructor
    static class TestDto {
        private String data;
    }

    @GetMapping("/dto")
    public TestDto dto(){
        return new TestDto("hello");
    }

    @GetMapping("/baseResponse")
    public BaseResponse<TestDto> baseResponse(){
        return BaseResponse.success(new TestDto("hello"));
    }
}
