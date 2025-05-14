package io.github.alberes.register.manager.frontend.controllers.dto;

import java.util.HashMap;
import java.util.Map;

public class UserSessionDto {

    private TokenDto token;

    private Map<String, Object> cache;

    public UserSessionDto(){
        this.cache = new HashMap<String, Object>();
    }

    public UserSessionDto(TokenDto token) {
        this.token = token;
    }

    public TokenDto getToken() {
        return token;
    }

    public Map<String, Object> getCache() {
        if(this.cache == null){
            this.cache = new HashMap<String, Object>();
        }
        return cache;
    }
}
