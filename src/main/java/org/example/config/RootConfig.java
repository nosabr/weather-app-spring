package org.example.config;

import org.example.util.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {
        "org.example.service",
        "org.example.dao",
        "org.example.util"
})
@Import({AppConfig.class})
public class RootConfig {
}