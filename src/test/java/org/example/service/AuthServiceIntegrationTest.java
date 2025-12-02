package org.example.service;

import org.example.BaseIntegrationTest;
import org.example.model.User;
import org.example.model.UserSession;
import org.example.util.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class AuthServiceIntegrationTest extends BaseIntegrationTest {
//
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    @DisplayName("✅ Успешная аутентификация")
//    void testSuccessfulAuth() {
//        User user = new User("testuser", passwordEncoder.encode("password123"));
//        userDao.save(user);
//
//        User auth = authService.authenticate("testuser", "password123");
//
//        assertThat(auth).isNotNull();
//        assertThat(auth.getLogin()).isEqualTo("testuser");
//    }
//
//    @Test
//    @DisplayName("❌ Неверный пароль")
//    void testWrongPassword() {
//        User user = new User("testuser", passwordEncoder.encode("correct"));
//        userDao.save(user);
//
//        User auth = authService.authenticate("testuser", "wrong");
//        assertThat(auth).isNull();
//    }
//
//    @Test
//    @DisplayName("❌ Несуществующий логин")
//    void testNonExistentLogin() {
//        User auth = authService.authenticate("nonexistent", "password123");
//        assertThat(auth).isNull();
//    }
//
//    @Test
//    @DisplayName("❌ Null логин")
//    void testNullLogin() {
//        User auth = authService.authenticate(null, "password123");
//        assertThat(auth).isNull();
//    }
//
//    @Test
//    @DisplayName("❌ Null пароль")
//    void testNullPassword() {
//        User user = new User("testuser", passwordEncoder.encode("password123"));
//        userDao.save(user);
//
//        User auth = authService.authenticate("testuser", null);
//        assertThat(auth).isNull();
//    }
//
//    @Test
//    @DisplayName("✅ Создание сессии")
//    void testCreateSession() {
//        User user = new User("testuser", "hash");
//        userDao.save(user);
//
//        UserSession session = authService.createSession(user);
//
//        assertThat(session).isNotNull();
//        assertThat(session.getId()).isNotNull();
//        assertThat(userSessionDao.findById(session.getId())).isPresent();
//    }
//
//    @Test
//    @DisplayName("✅ Получение по session ID")
//    void testGetBySessionId() {
//        User user = new User("testuser", "hash");
//        userDao.save(user);
//
//        UserSession session = authService.createSession(user);
//        User found = authService.getUserBySessionId(session.getId());
//
//        assertThat(found).isNotNull();
//        assertThat(found.getId()).isEqualTo(user.getId());
//    }
//
//    @Test
//    @DisplayName("❌ Несуществующий session ID")
//    void testNonExistentSessionId() {
//        User found = authService.getUserBySessionId(UUID.randomUUID());
//        assertThat(found).isNull();
//    }
//
//    @Test
//    @DisplayName("❌ Null session ID")
//    void testNullSessionId() {
//        User found = authService.getUserBySessionId(null);
//        assertThat(found).isNull();
//    }
//
//    @Test
//    @DisplayName("⭐ Истекшая сессия удаляется")
//    void testExpiredSessionDeleted() {
//        User user = new User("testuser", "hash");
//        userDao.save(user);
//
//        Instant expired = Instant.now().minus(1, ChronoUnit.HOURS);
//        UserSession session = new UserSession(user, expired);
//        userSessionDao.save(session);
//
//        User found = authService.getUserBySessionId(session.getId());
//
//        assertThat(found).isNull();
//        assertThat(userSessionDao.findById(session.getId())).isEmpty();
//    }
//
//    @Test
//    @DisplayName("⭐ Множественные истекшие сессии")
//    void testMultipleExpired() {
//        User user = new User("testuser", "hash");
//        userDao.save(user);
//
//        for (int i = 1; i <= 3; i++) {
//            Instant expired = Instant.now().minus(i, ChronoUnit.HOURS);
//            UserSession session = new UserSession(user, expired);
//            userSessionDao.save(session);
//
//            authService.getUserBySessionId(session.getId());
//            assertThat(userSessionDao.findById(session.getId())).isEmpty();
//        }
//    }
//
//    @Test
//    @DisplayName("✅ Валидная сессия НЕ удаляется")
//    void testValidNotDeleted() {
//        User user = new User("testuser", "hash");
//        userDao.save(user);
//
//        Instant future = Instant.now().plus(1, ChronoUnit.DAYS);
//        UserSession session = new UserSession(user, future);
//        userSessionDao.save(session);
//
//        User found = authService.getUserBySessionId(session.getId());
//
//        assertThat(found).isNotNull();
//        assertThat(userSessionDao.findById(session.getId())).isPresent();
//    }
//
//    @Test
//    @DisplayName("✅ Logout удаляет сессию")
//    void testLogout() {
//        User user = new User("testuser", "hash");
//        userDao.save(user);
//
//        UserSession session = authService.createSession(user);
//        authService.logout(session.getId().toString());
//
//        assertThat(userSessionDao.findById(session.getId())).isEmpty();
//    }
//
//    @Test
//    @DisplayName("❌ Logout с невалидным ID")
//    void testLogoutInvalidId() {
//        assertThatCode(() -> authService.logout("invalid")).doesNotThrowAnyException();
//    }
//
//    @Test
//    @DisplayName("❌ Logout с null")
//    void testLogoutNull() {
//        assertThatCode(() -> authService.logout(null)).doesNotThrowAnyException();
//    }
//
//    @Test
//    @DisplayName("❌ Logout несуществующего")
//    void testLogoutNonExistent() {
//        assertThatCode(() -> authService.logout(UUID.randomUUID().toString()))
//                .doesNotThrowAnyException();
//    }
//
//    @Test
//    @DisplayName("✅ Logout не влияет на другие")
//    void testLogoutOneSession() {
//        User user = new User("testuser", "hash");
//        userDao.save(user);
//
//        UserSession s1 = authService.createSession(user);
//        UserSession s2 = authService.createSession(user);
//        UserSession s3 = authService.createSession(user);
//
//        authService.logout(s2.getId().toString());
//
//        assertThat(userSessionDao.findById(s1.getId())).isPresent();
//        assertThat(userSessionDao.findById(s2.getId())).isEmpty();
//        assertThat(userSessionDao.findById(s3.getId())).isPresent();
//    }
//
//    @Test
//    @DisplayName("✅ Множественные сессии пользователя")
//    void testMultipleSessions() {
//        User user = new User("testuser", "hash");
//        userDao.save(user);
//
//        UserSession s1 = authService.createSession(user);
//        UserSession s2 = authService.createSession(user);
//        UserSession s3 = authService.createSession(user);
//
//        User u1 = authService.getUserBySessionId(s1.getId());
//        User u2 = authService.getUserBySessionId(s2.getId());
//        User u3 = authService.getUserBySessionId(s3.getId());
//
//        assertThat(u1.getId()).isEqualTo(user.getId());
//        assertThat(u2.getId()).isEqualTo(user.getId());
//        assertThat(u3.getId()).isEqualTo(user.getId());
//    }
}