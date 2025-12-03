package org.example.service;

import org.example.BaseIntegrationTest;
import org.example.model.User;
import org.example.model.UserSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class UserSessionServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserSessionService userSessionService;

    // ========== БАЗОВЫЕ ТЕСТЫ ==========

    @Test
    @DisplayName("✅ Создание сессии")
    void testCreateSession() {
        User user = new User("testuser", "hash");
        userDao.save(user);

        UserSession session = userSessionService.createUserSession(user);

        assertThat(session).isNotNull();
        assertThat(session.getId()).isNotNull();
        assertThat(session.getUser().getId()).isEqualTo(user.getId());
        assertThat(session.getExpiresAt()).isAfter(Instant.now());
        assertThat(userSessionDao.findById(session.getId())).isPresent();
    }

    @Test
    @DisplayName("✅ Получение пользователя по session ID")
    void testGetUserBySessionId() {
        User user = new User("testuser", "hash");
        userDao.save(user);

        UserSession session = userSessionService.createUserSession(user);
        User found = userSessionService.getUserBySessionId(session.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(user.getId());
        assertThat(found.getLogin()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("❌ Несуществующий session ID")
    void testNonExistentSessionId() {
        User found = userSessionService.getUserBySessionId(UUID.randomUUID());
        assertThat(found).isNull();
    }

    @Test
    @DisplayName("❌ Null session ID")
    void testNullSessionId() {
        User found = userSessionService.getUserBySessionId(null);
        assertThat(found).isNull();
    }

    // ========== ТЕСТЫ С ВРЕМЕНЕМ ⏰ ==========

    @Test
    @DisplayName("⏰ Сессия автоматически удаляется через 2 секунды")
    void testSessionExpiresAfter2Seconds() throws InterruptedException {
        User user = new User("testuser", "hash");
        userDao.save(user);

        // ⭐ Создаем сессию с временем жизни 2 секунды
        Instant expiresAt = Instant.now().plusSeconds(2);
        UserSession session = new UserSession(user, expiresAt);
        userSessionDao.save(session);
        UUID sessionId = session.getId();

        // Сразу после создания сессия существует
        User found1 = userSessionService.getUserBySessionId(sessionId);
        assertThat(found1).isNotNull();
        assertThat(found1.getLogin()).isEqualTo("testuser");
        System.out.println("✅ Сессия создана, время жизни: 2 секунды");

        System.out.println("⏳ Ждем 3 секунды...");
        Thread.sleep(3000);  // Ждем 3 секунды (2 + запас)
        System.out.println("⏰ Прошло 3 секунды, проверяем...");

        // После истечения времени сессия автоматически удаляется
        User found2 = userSessionService.getUserBySessionId(sessionId);

        assertThat(found2).isNull();
        assertThat(userSessionDao.findById(sessionId)).isEmpty();
        System.out.println("✅ Сессия автоматически удалена!");
    }

    @Test
    @DisplayName("⏰ Проверка момента истечения сессии")
    void testSessionDeletedExactlyOnExpiry() throws InterruptedException {
        User user = new User("testuser", "hash");
        userDao.save(user);

        // Создаем сессию с временем жизни 1 секунда
        Instant expiresAt = Instant.now().plusSeconds(1);
        UserSession session = new UserSession(user, expiresAt);
        userSessionDao.save(session);
        UUID sessionId = session.getId();

        // Через 0.5 секунды сессия еще жива
        System.out.println("⏳ Проверяем через 0.5 секунды...");
        Thread.sleep(500);
        User found1 = userSessionService.getUserBySessionId(sessionId);
        assertThat(found1).isNotNull();
        System.out.println("✅ Через 0.5 сек: сессия еще жива");

        // Ждем еще 1 секунду (всего 1.5 сек с момента создания)
        System.out.println("⏳ Ждем еще 1 секунду...");
        Thread.sleep(1000);
        System.out.println("⏰ Прошло 1.5 секунды с момента создания");

        // Теперь сессия должна быть удалена
        User found2 = userSessionService.getUserBySessionId(sessionId);
        assertThat(found2).isNull();
        assertThat(userSessionDao.findById(sessionId)).isEmpty();
        System.out.println("✅ Через 1.5 сек: сессия удалена!");
    }

    @Test
    @DisplayName("⏰ Несколько сессий с разным временем жизни")
    void testMultipleSessionsWithDifferentExpiry() throws InterruptedException {
        User user = new User("testuser", "hash");
        userDao.save(user);

        // Создаем 3 сессии с разным временем
        Instant expires1sec = Instant.now().plusSeconds(1);
        Instant expires2sec = Instant.now().plusSeconds(2);
        Instant expires3sec = Instant.now().plusSeconds(3);

        UserSession session1 = new UserSession(user, expires1sec);
        UserSession session2 = new UserSession(user, expires2sec);
        UserSession session3 = new UserSession(user, expires3sec);

        userSessionDao.save(session1);
        userSessionDao.save(session2);
        userSessionDao.save(session3);

        System.out.println("✅ Создано 3 сессии: 1сек, 2сек, 3сек");

        // Через 1.5 секунды: session1 удалена, остальные живы
        Thread.sleep(1500);
        System.out.println("⏰ Прошло 1.5 секунды");
        assertThat(userSessionService.getUserBySessionId(session1.getId())).isNull();
        assertThat(userSessionService.getUserBySessionId(session2.getId())).isNotNull();
        assertThat(userSessionService.getUserBySessionId(session3.getId())).isNotNull();
        System.out.println("✅ session1 удалена, session2 и session3 живы");

        // Через 2.5 секунды: session1, session2 удалены, session3 жива
        Thread.sleep(1000);
        System.out.println("⏰ Прошло 2.5 секунды");
        assertThat(userSessionService.getUserBySessionId(session1.getId())).isNull();
        assertThat(userSessionService.getUserBySessionId(session2.getId())).isNull();
        assertThat(userSessionService.getUserBySessionId(session3.getId())).isNotNull();
        System.out.println("✅ session1 и session2 удалены, session3 жива");

        // Через 3.5 секунды: все удалены
        Thread.sleep(1000);
        System.out.println("⏰ Прошло 3.5 секунды");
        assertThat(userSessionService.getUserBySessionId(session1.getId())).isNull();
        assertThat(userSessionService.getUserBySessionId(session2.getId())).isNull();
        assertThat(userSessionService.getUserBySessionId(session3.getId())).isNull();
        System.out.println("✅ Все сессии удалены!");
    }

    @Test
    @DisplayName("⏰ Сессия НЕ удаляется до истечения времени")
    void testSessionNotDeletedBeforeExpiry() throws InterruptedException {
        User user = new User("testuser", "hash");
        userDao.save(user);

        // Создаем сессию с временем жизни 10 секунд
        Instant expiresAt = Instant.now().plusSeconds(10);
        UserSession session = new UserSession(user, expiresAt);
        userSessionDao.save(session);
        UUID sessionId = session.getId();

        System.out.println("✅ Сессия создана, время жизни: 10 секунд");

        // Проверяем через 1, 2, 3 секунды - сессия должна быть жива
        for (int i = 1; i <= 3; i++) {
            Thread.sleep(1000);
            User found = userSessionService.getUserBySessionId(sessionId);
            assertThat(found).isNotNull();
            System.out.println("✅ Через " + i + " сек: сессия еще жива");
        }

        System.out.println("✅ Сессия жива на протяжении 3 секунд (из 10)");
    }

    // ========== ТЕСТЫ УДАЛЕНИЯ ==========

    @Test
    @DisplayName("⭐ Истекшая сессия (прошлое) удаляется сразу")
    void testExpiredSessionDeleted() {
        User user = new User("testuser", "hash");
        userDao.save(user);

        Instant expired = Instant.now().minus(1, ChronoUnit.HOURS);
        UserSession session = new UserSession(user, expired);
        userSessionDao.save(session);

        UUID sessionId = session.getId();
        User found = userSessionService.getUserBySessionId(sessionId);

        assertThat(found).isNull();
        assertThat(userSessionDao.findById(sessionId)).isEmpty();
    }

    @Test
    @DisplayName("✅ Валидная сессия НЕ удаляется")
    void testValidSessionNotDeleted() {
        User user = new User("testuser", "hash");
        userDao.save(user);

        UserSession session = userSessionService.createUserSession(user);
        User found = userSessionService.getUserBySessionId(session.getId());

        assertThat(found).isNotNull();
        assertThat(userSessionDao.findById(session.getId())).isPresent();
    }

    @Test
    @DisplayName("✅ Удаление сессии (logout)")
    void testDeleteSession() {
        User user = new User("testuser", "hash");
        userDao.save(user);

        UserSession session = userSessionService.createUserSession(user);
        UUID sessionId = session.getId();

        assertThat(userSessionDao.findById(sessionId)).isPresent();

        userSessionService.deleteUserSession(sessionId.toString());

        assertThat(userSessionDao.findById(sessionId)).isEmpty();
    }

    @Test
    @DisplayName("❌ Удаление с невалидным ID")
    void testDeleteInvalidSessionId() {
        assertThatCode(() -> userSessionService.deleteUserSession("invalid-uuid"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("❌ Удаление с null")
    void testDeleteNullSessionId() {
        assertThatCode(() -> userSessionService.deleteUserSession(null))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("❌ Удаление несуществующей сессии")
    void testDeleteNonExistentSession() {
        assertThatCode(() -> userSessionService.deleteUserSession(UUID.randomUUID().toString()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("✅ Удаление одной сессии не влияет на другие")
    void testDeleteOneSession() {
        User user = new User("testuser", "hash");
        userDao.save(user);

        UserSession s1 = userSessionService.createUserSession(user);
        UserSession s2 = userSessionService.createUserSession(user);
        UserSession s3 = userSessionService.createUserSession(user);

        userSessionService.deleteUserSession(s2.getId().toString());

        assertThat(userSessionDao.findById(s1.getId())).isPresent();
        assertThat(userSessionDao.findById(s2.getId())).isEmpty();
        assertThat(userSessionDao.findById(s3.getId())).isPresent();
    }

    @Test
    @DisplayName("✅ Множественные сессии одного пользователя")
    void testMultipleSessionsPerUser() {
        User user = new User("testuser", "hash");
        userDao.save(user);

        UserSession s1 = userSessionService.createUserSession(user);
        UserSession s2 = userSessionService.createUserSession(user);
        UserSession s3 = userSessionService.createUserSession(user);

        User u1 = userSessionService.getUserBySessionId(s1.getId());
        User u2 = userSessionService.getUserBySessionId(s2.getId());
        User u3 = userSessionService.getUserBySessionId(s3.getId());

        assertThat(u1.getId()).isEqualTo(user.getId());
        assertThat(u2.getId()).isEqualTo(user.getId());
        assertThat(u3.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("✅ Разные пользователи - разные сессии")
    void testDifferentUsersHaveDifferentSessions() {
        User user1 = new User("userone", "hash1");
        User user2 = new User("usertwo", "hash2");
        userDao.save(user1);
        userDao.save(user2);

        UserSession session1 = userSessionService.createUserSession(user1);
        UserSession session2 = userSessionService.createUserSession(user2);

        assertThat(session1.getId()).isNotEqualTo(session2.getId());

        User found1 = userSessionService.getUserBySessionId(session1.getId());
        User found2 = userSessionService.getUserBySessionId(session2.getId());

        assertThat(found1.getLogin()).isEqualTo("userone");
        assertThat(found2.getLogin()).isEqualTo("usertwo");
    }
}