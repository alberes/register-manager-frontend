package io.github.alberes.register.manager.frontend.config;

import io.github.alberes.register.manager.frontend.services.exceptions.RegisterManagerErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public RegisterManagerErrorDecoder registerManagerErrorDecoder() {
        return new RegisterManagerErrorDecoder();
    }
}