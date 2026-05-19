package com.consultas.agendamento_api.config;

import java.net.URI;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(Environment environment) {
        String jdbcUrl = property(environment, "SPRING_DATASOURCE_URL", "spring.datasource.url");
        String username = property(environment, "SPRING_DATASOURCE_USERNAME", "spring.datasource.username");
        String password = property(environment, "SPRING_DATASOURCE_PASSWORD", "spring.datasource.password");
        String driverClassName = property(
                environment,
                "SPRING_DATASOURCE_DRIVER_CLASS_NAME",
                "spring.datasource.driver-class-name");

        if (!StringUtils.hasText(jdbcUrl)) {
            String railwayUrl = environment.getProperty("DATABASE_PUBLIC_URL");
            if (!StringUtils.hasText(railwayUrl)) {
                railwayUrl = environment.getProperty("DATABASE_URL");
            }

            if (StringUtils.hasText(railwayUrl)) {
                DatabaseSettings settings = parseRailwayUrl(railwayUrl);
                jdbcUrl = settings.jdbcUrl();
                username = firstNonBlank(username, settings.username(), environment.getProperty("PGUSER"));
                password = firstNonBlank(password, settings.password(), environment.getProperty("PGPASSWORD"));
            }
        }

        if (!StringUtils.hasText(jdbcUrl)) {
            jdbcUrl = "jdbc:postgresql://localhost:5432/agendamento";
        }

        if (!StringUtils.hasText(username)) {
            username = firstNonBlank(environment.getProperty("PGUSER"), "postgres", "postgres");
        }

        if (!StringUtils.hasText(password)) {
            password = firstNonBlank(environment.getProperty("PGPASSWORD"), "postgres", "postgres");
        }

        if (!StringUtils.hasText(driverClassName)) {
            driverClassName = jdbcUrl.startsWith("jdbc:h2:") ? "org.h2.Driver" : "org.postgresql.Driver";
        }

        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }

    private DatabaseSettings parseRailwayUrl(String rawUrl) {
        URI uri = URI.create(rawUrl);
        String[] credentials = uri.getUserInfo().split(":", 2);
        String path = uri.getPath() == null ? "" : uri.getPath();
        String database = path.startsWith("/") ? path.substring(1) : path;

        String jdbcUrl = "jdbc:postgresql://%s:%d/%s?sslmode=require".formatted(
                uri.getHost(),
                uri.getPort(),
                database);

        return new DatabaseSettings(jdbcUrl, credentials[0], credentials.length > 1 ? credentials[1] : "");
    }

    private String firstNonBlank(String first, String second, String fallback) {
        if (StringUtils.hasText(first)) {
            return first;
        }
        if (StringUtils.hasText(second)) {
            return second;
        }
        return fallback;
    }

    private String property(Environment environment, String uppercaseKey, String canonicalKey) {
        String value = environment.getProperty(uppercaseKey);
        if (StringUtils.hasText(value)) {
            return value;
        }
        return environment.getProperty(canonicalKey);
    }

    private record DatabaseSettings(String jdbcUrl, String username, String password) {
    }
}
