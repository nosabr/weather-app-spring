package org.example.config;

import org.example.util.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {
        "org.example.service",
        "org.example.dao"
})
@Import({PersistenceConfig.class})
public class RootConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {};
    }
}