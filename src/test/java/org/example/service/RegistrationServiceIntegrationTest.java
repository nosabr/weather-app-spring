package org.example.service;

import org.example.BaseIntegrationTest;
import org.example.dto.RegistrationError;
import org.example.dto.RegistrationResultDTO;
import org.example.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

class RegistrationServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RegistrationService registrationService;

    @Test
    @DisplayName("✅ Успешная регистрация")
    void testSuccessfulRegistration() {
        RegistrationResultDTO result = registrationService.register("testuser", "password123");

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getUser()).isNotNull();
        assertThat(userDao.findByLogin("testuser")).isPresent();
    }

    @Test
    @DisplayName("❌ Дублирующийся логин")
    void testDuplicateLogin() {
        registrationService.register("duplicate", "password123");
        RegistrationResultDTO result = registrationService.register("duplicate", "password123");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getRegistrationError()).isEqualTo(RegistrationError.LOGIN_IS_TAKEN);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "x", ""})
    @DisplayName("❌ Короткий логин")
    void testShortLogin(String login) {
        RegistrationResultDTO result = registrationService.register(login, "password123");
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getRegistrationError()).isEqualTo(RegistrationError.INVALID_LOGIN_FORMAT);
    }

    @Test
    @DisplayName("❌ Null логин")
    void testNullLogin() {
        RegistrationResultDTO result = registrationService.register(null, "password123");
        assertThat(result.isSuccess()).isFalse();
    }

    @Test
    @DisplayName("❌ Пустой логин")
    void testEmptyLogin() {
        RegistrationResultDTO result = registrationService.register("", "password123");
        assertThat(result.isSuccess()).isFalse();
    }

    @Test
    @DisplayName("❌ Логин из пробелов")
    void testWhitespaceLogin() {
        RegistrationResultDTO result = registrationService.register("   ", "password123");
        assertThat(result.isSuccess()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "pwd", "a", ""})
    @DisplayName("❌ Короткий пароль")
    void testShortPassword(String password) {
        RegistrationResultDTO result = registrationService.register("testuser", password);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getRegistrationError()).isEqualTo(RegistrationError.INVALID_PASSWORD_FORMAT);
    }

    @Test
    @DisplayName("❌ Null пароль")
    void testNullPassword() {
        RegistrationResultDTO result = registrationService.register("testuser", null);
        assertThat(result.isSuccess()).isFalse();
    }

    @Test
    @DisplayName("✅ Минимальные значения")
    void testMinimalValid() {
        RegistrationResultDTO result = registrationService.register("abcdef", "123456");
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("✅ Длинные значения")
    void testLongValues() {
        RegistrationResultDTO result = registrationService.register("a".repeat(50), "p".repeat(100));
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("✅ Спецсимволы в пароле")
    void testSpecialCharsPassword() {
        RegistrationResultDTO result = registrationService.register("testuser", "P@ssw0rd!#$%");
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("✅ Разные хеши для одинаковых паролей")
    void testDifferentHashes() {
        RegistrationResultDTO r1 = registrationService.register("user1", "samePassword");
        RegistrationResultDTO r2 = registrationService.register("user2", "samePassword");
        assertThat(r1.getUser().getPassword()).isNotEqualTo(r2.getUser().getPassword());
    }

    @Test
    @DisplayName("✅ Множественная регистрация")
    void testMultipleUsers() {
        for (int i = 1; i <= 5; i++) {
            RegistrationResultDTO result = registrationService.register("user" + i, "password123");
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    @DisplayName("✅ Пароль хешируется BCrypt")
    void testPasswordHashing() {
        RegistrationResultDTO result = registrationService.register("testuser", "password123");

        assertThat(result.getUser().getPassword()).startsWith("$2a$");
        assertThat(result.getUser().getPassword()).isNotEqualTo("password123");
    }

    @Test
    @DisplayName("✅ ID присваивается")
    void testIdAssigned() {
        RegistrationResultDTO result = registrationService.register("testuser", "password123");

        assertThat(result.getUser().getId()).isNotNull();
        assertThat(result.getUser().getId()).isPositive();
    }

    @Test
    @DisplayName("✅ Пользователь сохраняется в БД")
    void testUserSavedToDatabase() {
        registrationService.register("testuser", "password123");

        User saved = userDao.findByLogin("testuser").orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getLogin()).isEqualTo("testuser");
    }
}