package org.example.dto;

import lombok.Getter;
import org.example.dto.AuthError;
import org.example.model.User;

@Getter
public class AuthResultDTO {
    private final User user;
    private final AuthError authError;

    private AuthResultDTO(User user, AuthError authError) {
        this.user = user;
        this.authError = authError;
    }

    public static AuthResultDTO success(User user){
        return new AuthResultDTO(user, null);
    }
    public static AuthResultDTO failure(AuthError authError){
        return new AuthResultDTO(null, authError);
    }
}
