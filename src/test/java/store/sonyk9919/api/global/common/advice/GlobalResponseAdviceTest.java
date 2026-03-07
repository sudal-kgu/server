package store.sonyk9919.api.global.common.advice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GlobalResponseAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("DTO 반환 시 BaseResponse 형식으로 래핑된다")
    void wrappedDto() throws Exception {
        mockMvc.perform(get("/advice/dto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON_200"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.data.data").value("hello"));
    }

    @Test
    @DisplayName("이미 BaseResponse로 래핑된 경우 이중 래핑되지 않는다")
    void notDupWrapped() throws Exception {
        mockMvc.perform(get("/advice/baseResponse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON_200"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.data.data").value("hello"))
                .andExpect(jsonPath("$.data.code").doesNotExist());
    }
}