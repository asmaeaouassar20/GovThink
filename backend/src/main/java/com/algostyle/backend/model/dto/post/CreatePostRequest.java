package com.algostyle.backend.model.dto.post;

import jakarta.validation.constraints.NotBlank;

public class CreatePostRequest {

    private String title;

    @NotBlank(message = "Le contenu est obligatoire")
    private String content;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString(){
        return "title:"+title+ " - content:"+content+" .\n";
    }
}
