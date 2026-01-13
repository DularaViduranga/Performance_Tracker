package com.dulara.figure_controller.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.dulara.figure_controller.repository.mysql",
        entityManagerFactoryRef = "mysqlEntityManagerFactory",
        transactionManagerRef = "mysqlTransactionManager"
)
public class MySqlDataSourceConfig {

    @Primary
    @Bean(name = "mysqlDataSourceProperties")
    @ConfigurationProperties("spring.datasource.mysql")
    public DataSourceProperties mysqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties("spring.datasource.mysql.hikari")
    public DataSource mysqlDataSource(@Qualifier("mysqlDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("mysqlDataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        //properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);

        return builder
                .dataSource(dataSource)
                .packages("com.dulara.figure_controller.entity")
                .persistenceUnit("mysql")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager mysqlTransactionManager(
            @Qualifier("mysqlEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
