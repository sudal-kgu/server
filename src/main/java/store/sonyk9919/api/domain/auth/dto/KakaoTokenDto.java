package store.sonyk9919.api.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class KakaoTokenDto {


    @Getter
    public static class RequestBodyDto {

        private final String grantType = "authorization_code";
        private final String clientId;
        private final String code;
        private final String clientSecret;
        private final String redirectUri;

        private RequestBodyDto(String clientId, String code, String clientSecret, String redirectUri) {
            this.clientId = clientId;
            this.code = code;
            this.clientSecret = clientSecret;
            this.redirectUri = redirectUri;
        }

        public static RequestBodyDto from(String clientId, String code, String clientSecret, String redirectUri) {
            return new RequestBodyDto(clientId, code, clientSecret, redirectUri);
        }

        public MultiValueMap<String, String> toFormData() {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", grantType);
            map.add("client_id", clientId);
            map.add("code", code);
            map.add("client_secret", clientSecret);
            return map;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TokenResponseDto {

        @JsonProperty("access_token")
        private String accessToken;
    }
}
