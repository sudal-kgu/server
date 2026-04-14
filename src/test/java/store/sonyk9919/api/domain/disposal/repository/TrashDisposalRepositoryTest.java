package store.sonyk9919.api.domain.disposal.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import store.sonyk9919.api.global.config.QueryDslConfig;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
class TrashDisposalRepositoryTest {

    @Autowired
    private TrashDisposalRepository trashDisposalRepository;

    @Test
    @DisplayName("전체 Disposal은 총 36개")
    public void isCountCorrect() { assertThat(trashDisposalRepository.count()).isEqualTo(36); }
}