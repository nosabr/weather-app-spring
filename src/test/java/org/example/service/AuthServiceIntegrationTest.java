package org.example.service;

import org.example.BaseIntegrationTest;
import org.example.dto.AuthError;
import org.example.dto.AuthResultDTO;
import org.example.model.User;
import org.example.util.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

class AuthServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("✅ Успешная аутентификация")
    void testSuccessfulAuth() {
        User user = new User("testuser", passwordEncoder.encode("password123"));
        userDao.save(user);

        AuthResultDTO result = authService.authenticate("testuser", "password123");

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getUser()).isNotNull();
        assertThat(result.getUser().getLogin()).isEqualTo("testuser");
        assertThat(result.getAuthError()).isNull();
    }

    @Test
    @DisplayName("❌ Неверный пароль")
    void testWrongPassword() {
        User user = new User("testuser", passwordEncoder.encode("correct"));
        userDao.save(user);

        AuthResultDTO result = authService.authenticate("testuser", "wrong");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getUser()).isNull();
        assertThat(result.getAuthError()).isEqualTo(AuthError.WRONG_PASSWORD);
    }

    @Test
    @DisplayName("❌ Несуществующий логин")
    void testNonExistentLogin() {
        AuthResultDTO result = authService.authenticate("nonexistent", "password123");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getUser()).isNull();
        assertThat(result.getAuthError()).isEqualTo(AuthError.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("❌ Null логин")
    void testNullLogin() {
        AuthResultDTO result = authService.authenticate(null, "password123");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getUser()).isNull();
        // Может быть NPE или USER_NOT_FOUND - зависит от реализации
    }

    @Test
    @DisplayName("❌ Null пароль")
    void testNullPassword() {
        User user = new User("testuser", passwordEncoder.encode("password123"));
        userDao.save(user);

        AuthResultDTO result = authService.authenticate("testuser", null);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getUser()).isNull();
        // BCrypt.checkpw может бросить NPE или вернуть false
    }

    @Test
    @DisplayName("✅ Аутентификация с разными пользователями")
    void testMultipleUsers() {
        User user1 = new User("user1", passwordEncoder.encode("password1"));
        User user2 = new User("user2", passwordEncoder.encode("password2"));
        userDao.save(user1);
        userDao.save(user2);

        AuthResultDTO result1 = authService.authenticate("user1", "password1");
        AuthResultDTO result2 = authService.authenticate("user2", "password2");

        assertThat(result1.isSuccess()).isTrue();
        assertThat(result2.isSuccess()).isTrue();
        assertThat(result1.getUser().getLogin()).isEqualTo("user1");
        assertThat(result2.getUser().getLogin()).isEqualTo("user2");
    }

    @Test
    @DisplayName("❌ Пустой логин")
    void testEmptyLogin() {
        AuthResultDTO result = authService.authenticate("", "password123");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getAuthError()).isEqualTo(AuthError.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("❌ Пустой пароль")
    void testEmptyPassword() {
        User user = new User("testuser", passwordEncoder.encode("password123"));
        userDao.save(user);

        AuthResultDTO result = authService.authenticate("testuser", "");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getAuthError()).isEqualTo(AuthError.WRONG_PASSWORD);
    }

    @Test
    @DisplayName("✅ BCrypt хеши проверяются правильно")
    void testBCryptValidation() {
        String rawPassword = "mySecurePassword123";
        String hashedPassword = passwordEncoder.encode(rawPassword);

        User user = new User("testuser", hashedPassword);
        userDao.save(user);

        AuthResultDTO result = authService.authenticate("testuser", rawPassword);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getUser().getPassword()).isEqualTo(hashedPassword);
    }
}