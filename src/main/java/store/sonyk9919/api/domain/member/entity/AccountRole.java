package store.sonyk9919.api.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.member.exception.MemberStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AccountRole {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String key;

    public static AccountRole findByKey(String key) {
        return Arrays.stream(AccountRole.values())
                .filter(role -> role.getKey().equals(key))
                .findAny()
                .orElseThrow(() -> new CustomException(MemberStatus.MEMBER_ACCOUNT_BAD_REQUEST));
    }
}
