package org.example.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableScheduling
@PropertySource("classpath:database.properties")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean
    @Profile({"dev", "default"})
    public DataSource devDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));

        dataSource.setInitialSize(Integer.parseInt(env.getProperty("db.initialSize")));
        dataSource.setMaxTotal(Integer.parseInt(env.getProperty("db.maxTotal")));
        dataSource.setMaxIdle(Integer.parseInt(env.getProperty("db.maxIdle")));
        dataSource.setMinIdle(Integer.parseInt(env.getProperty("db.minIdle")));

        System.out.println("🔧 [DEV] PostgreSQL configured");
        return dataSource;
    }

    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(10);

        System.out.println("🧪 [TEST] H2 in-memory configured");
        return dataSource;
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));

        dataSource.setInitialSize(10);
        dataSource.setMaxTotal(50);
        dataSource.setMaxIdle(20);
        dataSource.setMinIdle(10);

        System.out.println("🚀 [PROD] PostgreSQL configured");
        return dataSource;
    }

    @Bean(name = "sessionFactory")
    @Profile({"dev", "prod", "default"})
    @DependsOn("flyway")
    public LocalSessionFactoryBean sessionFactoryForDevProd(DataSource dataSource) {
        LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
        sf.setDataSource(dataSource);
        sf.setPackagesToScan("org.example.model");
        sf.setHibernateProperties(hibernatePropertiesForDevProd());
        return sf;
    }

    @Bean(name = "sessionFactory")
    @Profile("test")
    public LocalSessionFactoryBean sessionFactoryForTest(DataSource dataSource) {
        LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
        sf.setDataSource(dataSource);
        sf.setPackagesToScan("org.example.model");
        sf.setHibernateProperties(hibernatePropertiesForTest());
        return sf;
    }

    private Properties hibernatePropertiesForDevProd() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", "true");
        return properties;
    }

    private Properties hibernatePropertiesForTest() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        return properties;
    }

    @Bean
    public HibernateTransactionManager transactionManager(LocalSessionFactoryBean sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory.getObject());
        return transactionManager;
    }

    @Bean(initMethod = "migrate")
    @Profile({"dev", "prod", "default"})
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
    }
}