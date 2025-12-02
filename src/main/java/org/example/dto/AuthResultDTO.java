package org.example.dto;

import lombok.Getter;
import org.example.dto.AuthError;
import org.example.model.User;

@Getter
public class AuthResultDTO {
    private final boolean isSuccess;
    private final User user;
    private final AuthError authError;

    private AuthResultDTO(boolean isSuccess, User user, AuthError authError) {
        this.isSuccess = isSuccess;
        this.user = user;
        this.authError = authError;
    }

    public static AuthResultDTO success(User user){
        return new AuthResultDTO(true, user, null);
    }
    public static AuthResultDTO failure(AuthError authError){
        return new AuthResultDTO(false, null, authError);
    }
}
