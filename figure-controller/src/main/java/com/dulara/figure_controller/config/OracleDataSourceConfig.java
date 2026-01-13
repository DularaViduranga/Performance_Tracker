package com.dulara.figure_controller.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = "com.dulara.figure_controller.repository.oracle",
//        entityManagerFactoryRef = "oracleEntityManagerFactory",
//        transactionManagerRef = "oracleTransactionManager"
//)
public class OracleDataSourceConfig {

    @Bean(name = "oracleDataSourceProperties")
    @ConfigurationProperties("spring.datasource.oracle")
    public DataSourceProperties oracleDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "oracleDataSource")
    @ConfigurationProperties("spring.datasource.oracle.hikari")
    public DataSource oracleDataSource(@Qualifier("oracleDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    // Add JdbcTemplate for raw SQL queries to Oracle
    @Bean(name = "oracleJdbcTemplate")
    public JdbcTemplate oracleJdbcTemplate(@Qualifier("oracleDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
