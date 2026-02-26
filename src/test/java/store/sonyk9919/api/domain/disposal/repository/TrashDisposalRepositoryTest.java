package store.sonyk9919.api.domain.disposal.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TrashDisposalRepositoryTest {

    @Autowired
    private TrashDisposalRepository trashDisposalRepository;

    @Test
    @DisplayName("전체 Disposal은 총 36개")
    public void isCountCorrect() { assertThat(trashDisposalRepository.count()).isEqualTo(36); }
}