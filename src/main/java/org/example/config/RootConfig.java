package org.example.config;

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
    // Главная конфигурация приложения
    // Здесь регистрируются все бины кроме контроллеров
}