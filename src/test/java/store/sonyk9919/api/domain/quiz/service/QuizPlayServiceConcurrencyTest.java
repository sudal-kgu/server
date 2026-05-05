package store.sonyk9919.api.domain.quiz.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import store.sonyk9919.api.domain.auth.service.KakaoProvider;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.service.IslandLevelService;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.shop.service.ShopService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuizPlayServiceConcurrencyTest {

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
    private QuizPlayService quizPlayService;

    @Autowired
    private QuizSessionRegistryService quizSessionRegistryService;

    @Autowired
    private MemberIslandRegistryService memberIslandRegistryService;

    @Autowired
    private IslandLevelService islandLevelService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long memberAccountId;
    private Long islandId;
    private Long sessionId;
    private Long itemId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(
                "INSERT INTO member_account (account_role, provider_type, provider_id) VALUES (?, ?, ?)",
                "USER", "KAKAO", "test-concurrent");
        memberAccountId = jdbcTemplate.queryForObject(
                "SELECT member_account_id FROM member_account WHERE provider_id = ?",
                Long.class, "test-concurrent");

        jdbcTemplate.update(
                "INSERT INTO member_island (nickname, level, cumulative_exp, recycling_contribution_exp, item_contribution_exp, member_account_id) VALUES (?, ?, ?, ?, ?, ?)",
                "테스트섬", 1, 0, 0, 0, memberAccountId);
        islandId = jdbcTemplate.queryForObject(
                "SELECT island_id FROM member_island WHERE member_account_id = ?",
                Long.class, memberAccountId);

        jdbcTemplate.update("INSERT INTO member_resource (resource_type, amount, island_id) VALUES ('SHELL', 0, ?)", islandId);
        jdbcTemplate.update("INSERT INTO member_resource (resource_type, amount, island_id) VALUES ('FUEL', 0, ?)", islandId);

        itemId = jdbcTemplate.queryForObject("SELECT item_id FROM item LIMIT 1", Long.class);

        MemberIsland island = memberIslandRegistryService.getIsland(memberAccountId);
        islandLevelService.addRecyclingExp(island);

        sessionId = quizSessionRegistryService.create(island).getId();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM quiz_session WHERE island_id = ?", islandId);
        jdbcTemplate.update("DELETE FROM member_resource WHERE island_id = ?", islandId);
        jdbcTemplate.update("DELETE FROM member_island WHERE island_id = ?", islandId);
        jdbcTemplate.update("DELETE FROM member_account WHERE member_account_id = ?", memberAccountId);
    }

    @Test
    @Order(1)
    @DisplayName("동일 유저 동시 호출 시 보상이 정확히 1회만 지급된다")
    void completeQuizSession_동시_호출_시_보상이_정확히_1회만_지급된다() throws InterruptedException {
        // given
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        List<Exception> exceptions = new CopyOnWriteArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    quizPlayService.completeQuizSession(memberAccountId, sessionId);
                } catch (Exception e) {
                    exceptions.add(e);
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
        Long shell = jdbcTemplate.queryForObject(
                "SELECT amount FROM member_resource WHERE island_id = ? AND resource_type = 'SHELL'",
                Long.class, islandId);
        Long fuel = jdbcTemplate.queryForObject(
                "SELECT amount FROM member_resource WHERE island_id = ? AND resource_type = 'FUEL'",
                Long.class, islandId);

        assertThat(shell).isEqualTo(10L);
        assertThat(fuel).isEqualTo(100L);
        assertThat(exceptions).hasSize(threadCount - 1);
    }

    @Test
    @Order(2)
    @DisplayName("단일 호출 수행 시간이 수백 ms 이내다 (락 보유 시간 기준선)")
    void completeQuizSession_수행_시간이_수백ms_이내다() {
        // given / when
        long start = System.currentTimeMillis();
        quizPlayService.completeQuizSession(memberAccountId, sessionId);
        long elapsed = System.currentTimeMillis() - start;

        // then
        System.out.println("completeQuizSession 수행 시간: " + elapsed + "ms");
        assertThat(elapsed).isLessThan(500L);
    }
}
