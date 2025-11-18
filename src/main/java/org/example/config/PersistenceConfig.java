package org.example.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:database.properties")
public class PersistenceConfig {

    @Autowired
    private Environment env;

    // DataSource - пул соединений с БД
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));

        // Настройки пула соединений
        dataSource.setInitialSize(Integer.parseInt(env.getProperty("db.initialSize")));
        dataSource.setMaxTotal(Integer.parseInt(env.getProperty("db.maxTotal")));
        dataSource.setMaxIdle(Integer.parseInt(env.getProperty("db.maxIdle")));
        dataSource.setMinIdle(Integer.parseInt(env.getProperty("db.minIdle")));

        return dataSource;
    }

    // Hibernate SessionFactory
    @Bean
    @DependsOn("flyway")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("org.example.model"); // Сканирование Entity классов
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    // Hibernate Properties
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));

        // Важно! hbm2ddl.auto = validate означает, что Hibernate только проверяет схему БД
        // Создавать таблицы будет Flyway через миграции

        return properties;
    }

    // Transaction Manager
    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    // Flyway - миграции БД
    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(dataSource())
                .locations("classpath:db/migration") // Папка с миграциями
                .baselineOnMigrate(true) // Для существующих БД
                .load();
    }
}