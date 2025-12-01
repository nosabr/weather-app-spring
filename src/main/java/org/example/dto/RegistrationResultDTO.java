package org.example.dto;

import lombok.Getter;
import org.example.model.User;

@Getter
public class RegistrationResultDTO {
    private final User user;
    private final RegistrationError registrationError;

    private RegistrationResultDTO(User user, RegistrationError registrationError) {
        this.user = user;
        this.registrationError = registrationError;
    }

    public static RegistrationResultDTO success(User user){
        return new RegistrationResultDTO(user, null);
    }
    public static RegistrationResultDTO failure(RegistrationError registrationError){
        return new RegistrationResultDTO(null, registrationError);
    }
}
