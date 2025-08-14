package com.algostyle.backend.model.dto.post;

import com.algostyle.backend.model.entity.Comment;
import com.algostyle.backend.model.entity.Post;
import com.algostyle.backend.model.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    private String email;
    private String nom;
    private String prenom;
    private List<Long> idsSavedPost;


    public UserDTO(User user){
        this.email=user.getEmail();
        this.nom=user.getNom();
        this.prenom=user.getPrenom();
        this.idsSavedPost=user.getIdsSavedPost();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public List<Long> getIdsSavedPost() {
        return idsSavedPost;
    }

    public void setIdsSavedPost(List<Long> idsSavedPost) {
        this.idsSavedPost = idsSavedPost;
    }
}
