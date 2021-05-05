package com.spc.server.pojo;

import org.apache.shiro.authc.AuthenticationToken;

import java.util.Date;

public class SysToken implements AuthenticationToken {
    String serialVersionUID = "8939244780389542801";
    private String token;
    private String username;

    public SysToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}