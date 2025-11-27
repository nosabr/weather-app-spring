package org.example.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.annotation.Bean;

public class PasswordEncoder {
    private final int workFactor;
    public PasswordEncoder() {
        this.workFactor = 10;
    }

    public String encode(String rawPassword) {
        if(rawPassword == null){
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(workFactor));
    }

    public boolean check(String rawPassword, String encodedPassword) {
        if(rawPassword == null || encodedPassword == null){
            return false;
        }
        try{
            return BCrypt.checkpw(rawPassword, encodedPassword);
        } catch(IllegalArgumentException e){
            System.out.println("[PasswordEncoder] Invalid hash format");
            return false;
        }
    }
}
