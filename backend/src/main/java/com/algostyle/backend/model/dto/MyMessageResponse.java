package com.algostyle.backend.model.dto;

public class MyMessageResponse {
    private String message;

    public MyMessageResponse(String message){
        this.message=message;
    }

    public String getMessage(){
        return message;
    }

}
