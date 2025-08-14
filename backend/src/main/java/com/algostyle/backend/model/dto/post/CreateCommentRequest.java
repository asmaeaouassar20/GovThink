package com.algostyle.backend.model.dto.post;

import jakarta.validation.constraints.NotBlank;

public class CreateCommentRequest {

    @NotBlank(message = "Le contenu est obligatoire")
    private String content;




    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "content: "+content;
    }
}
