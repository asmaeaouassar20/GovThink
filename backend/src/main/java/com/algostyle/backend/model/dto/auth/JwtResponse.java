package com.algostyle.backend.model.dto.auth;

public class JwtResponse {
    private String token;

    public JwtResponse(String token){
        this.token=token;
    }


    public String getToken(){
        return this.token;
    }


    public void setToken(String token){
        this.token=token;
    }
}
