package org.example;

import org.example.config.RootConfig;
import org.example.dao.UserDao;
import org.example.dao.UserSessionDao;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

@SpringJUnitConfig(RootConfig.class)  // ⭐ RootConfig импортирует AppConfig
@ActiveProfiles("test")               // ⭐ Профиль TEST
@Transactional
public abstract class BaseIntegrationTest {

    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected UserSessionDao userSessionDao;

    @BeforeEach
    void setUp() {
        System.out.println("\n========================================");
        System.out.println("🧹 Cleaning database...");
        System.out.println("========================================");

        int sessions = sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM UserSession").executeUpdate();

        int users = sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM User").executeUpdate();

        System.out.println("🗑️  Deleted " + sessions + " sessions");
        System.out.println("🗑️  Deleted " + users + " users");
        System.out.println("✅ Database cleaned\n");
    }
}