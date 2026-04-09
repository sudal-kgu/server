package store.sonyk9919.api.domain.building.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EffectType {
    NONE("효과 없음"),

    QUIZ_REWARD_BOOST("퀴즈 보상 퍼센트 증가"),
    ISLAND_BOOST("섬 전체 생산량 퍼센트 증가"),

    DISPOSAL_REWARD_ADD("분리배출 보상 고정값 추가"),
    QUIZ_REWARD_ADD("퀴즈 보상 고정값 추가");

    private final String description;
}