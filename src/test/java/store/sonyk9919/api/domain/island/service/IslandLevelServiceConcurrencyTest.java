package store.sonyk9919.api.domain.island.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import store.sonyk9919.api.domain.auth.service.KakaoProvider;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.LockStatus;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class IslandLevelServiceConcurrencyTest {

    @Container
    private static final GenericContainer<?> redis =
            new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);

    @DynamicPropertySource
    static void setRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @MockitoBean
    private ConnectionFactory connectionFactory;

    @MockitoBean
    private KakaoProvider kakaoProvider;

    @Autowired
    private IslandLevelService islandLevelService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private TransactionTemplate transactionTemplate;

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
    void addRecyclingExp_동시_3회_호출_시_exp가_정확히_300_쌓인다() throws InterruptedException {
        // given
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

        // when
        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();

        // then
        Integer cumulativeExp = jdbcTemplate.queryForObject(
                "SELECT cumulative_exp FROM member_island WHERE member_account_id = ?",
                Integer.class, memberAccountId
        );
        assertThat(cumulativeExp).isEqualTo(expectedExp);
    }

    @Test
    @DisplayName("addRecyclingExp 락이 이미 점유 중이면 두 번째 호출은 409를 반환한다")
    void addRecyclingExp_락_점유_중_409_반환() throws InterruptedException {
        RLock lock = redissonClient.getLock("lock:island:" + memberAccountId + ":exp");
        lock.lock();

        AtomicReference<Throwable> thrown = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            try {
                transactionTemplate.execute(status -> {
                    islandLevelService.addRecyclingExp(memberAccountId);
                    return null;
                });
            } catch (Throwable e) {
                thrown.set(e);
            }
        });

        try {
            thread.start();
            thread.join();
        } finally {
            lock.unlock();
        }

        assertThat(thrown.get()).isInstanceOf(CustomException.class);
        assertThat(((CustomException) thrown.get()).getStatus()).isEqualTo(LockStatus.LOCK_ACQUISITION_FAILED);
    }
}
