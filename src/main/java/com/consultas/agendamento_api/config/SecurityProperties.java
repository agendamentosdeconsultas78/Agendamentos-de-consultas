package com.consultas.agendamento_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private final User admin = new User();
    private final User atendente = new User();

    public SecurityProperties() {
        atendente.setUsername("atendente");
        atendente.setPassword("atendente123");
    }

    public User getAdmin() {
        return admin;
    }

    public User getAtendente() {
        return atendente;
    }

    public static class User {
        private String username = "admin";
        private String password = "admin123";

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
