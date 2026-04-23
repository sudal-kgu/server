package store.sonyk9919.api.domain.island.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import store.sonyk9919.api.support.IntegrationTestSupport;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class IslandLevelServiceConcurrencyTest extends IntegrationTestSupport {

    @Autowired
    private IslandLevelService islandLevelService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long memberAccountId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(
                "INSERT INTO member_account (account_role, provider_type, provider_id) VALUES (?, ?, ?)",
                "USER", "KAKAO", "test-user-lock"
        );
        memberAccountId = jdbcTemplate.queryForObject(
                "SELECT member_account_id FROM member_account WHERE provider_id = ?",
                Long.class, "test-user-lock"
        );

        jdbcTemplate.update(
                "INSERT INTO member_island (nickname, level, cumulative_exp, recycling_contribution_exp, item_contribution_exp, member_account_id) VALUES (?, ?, ?, ?, ?, ?)",
                "테스트섬", 1, 0, 0, 0, memberAccountId
        );
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM member_island WHERE member_account_id = ?", memberAccountId);
        jdbcTemplate.update("DELETE FROM member_account WHERE member_account_id = ?", memberAccountId);
    }

    @Test
    @DisplayName("동시에 3번 addRecyclingExp를 호출해도 exp가 정확히 300 쌓인다")
    void concurrentAddRecyclingExp() throws InterruptedException {
        int threadCount = 3;
        int expectedExp = 300;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    islandLevelService.addRecyclingExp(memberAccountId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();

        Integer cumulativeExp = jdbcTemplate.queryForObject(
                "SELECT cumulative_exp FROM member_island WHERE member_account_id = ?",
                Integer.class, memberAccountId
        );

        assertThat(cumulativeExp).isEqualTo(expectedExp);
    }
}
