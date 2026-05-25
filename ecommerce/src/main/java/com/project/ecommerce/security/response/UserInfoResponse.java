package com.project.ecommerce.security.response;

import java.util.List;

public class UserInfoResponse {
    private Long id;
    private String jwtToken;
    private String email;
    private String username;
    private List<String> roles;

    public UserInfoResponse(Long id,String username, List<String> roles,String email, String jwtToken) {
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
        this.email=email;
        this.id=id;
    }

    public UserInfoResponse(Long id, String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
        this.id=id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


