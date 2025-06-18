package io.github.alberes.register.manager.frontend.controllers.dto;

import io.github.alberes.register.manager.frontend.constants.Constants;

import java.util.HashMap;
import java.util.Map;

public class UserSessionDto {

    private TokenDto token;

    private UserAccountProfileDto userAccountProfileDto;

    private Map<String, Object> cache;

    public UserSessionDto(){
        this.cache = new HashMap<String, Object>();
    }

    public UserSessionDto(TokenDto token) {
        this.token = token;
    }

    public UserSessionDto(TokenDto token, UserAccountProfileDto userAccountProfileDto) {
        this.token = token;
        this.userAccountProfileDto = userAccountProfileDto;
    }

    public UserAccountProfileDto getUserAccountProfileDto() {
        return userAccountProfileDto;
    }

    public void setUserAccountProfileDto(UserAccountProfileDto userAccountProfileDto) {
        this.userAccountProfileDto = userAccountProfileDto;
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

    public boolean isAdmin(){
        return this.userAccountProfileDto.getProfiles()
                .stream()
                .filter(p -> Constants.ADMIN.equals(p))
                .findFirst()
                .isPresent();
    }
}
