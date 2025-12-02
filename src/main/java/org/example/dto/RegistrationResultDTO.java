package org.example.dto;

import lombok.Getter;
import org.example.model.User;

@Getter
public class RegistrationResultDTO {
    private final boolean isSuccess;
    private final User user;
    private final RegistrationError registrationError;

    private RegistrationResultDTO(boolean isSuccess,User user, RegistrationError registrationError) {
        this.isSuccess = isSuccess;
        this.user = user;
        this.registrationError = registrationError;
    }

    public static RegistrationResultDTO success(User user){
        return new RegistrationResultDTO(true, user, null);
    }
    public static RegistrationResultDTO failure(RegistrationError registrationError){
        return new RegistrationResultDTO(false, null, registrationError);
    }
}
