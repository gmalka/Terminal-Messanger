package com.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:db.properties")
public class ApplicationConfig {

    @Value("${url}")
    private String url;

    @Value("${user}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${driverName}")
    private String driver;

    @Bean
    private HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        return config;
    }

    @Bean
    private HikariDataSource getHikariDataSource(HikariConfig config) {
        return new HikariDataSource(config);
    }

    @Bean
    private PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
