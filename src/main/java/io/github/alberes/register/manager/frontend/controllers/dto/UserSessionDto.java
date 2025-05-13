package io.github.alberes.register.manager.frontend.controllers.dto;

public class UserSessionDto {

    private TokenDto token;

    public UserSessionDto(TokenDto token) {
        this.token = token;
    }

    public TokenDto getToken() {
        return token;
    }
}
