package io.github.alberes.register.manager.frontend.controllers.dto;

import java.util.HashSet;
import java.util.Set;

public class UserAccountProfileDto{
    private String id;
    private String name;
    private String email;
    private Set<String> profiles;

    public UserAccountProfileDto() {
    }

    public UserAccountProfileDto(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profiles = new HashSet<String>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<String> profiles) {
        this.profiles = profiles;
    }
}
