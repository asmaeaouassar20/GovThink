package com.algostyle.backend.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
public class User {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @NotBlank(message = "L'email est obligatoire")
     @Email(message = "Le format de l'email est invalide")
     @Column(unique = true)
    private String email;

     @NotBlank(message = "Le mot de passe est obligatoire")
     @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
     @Column(nullable = false)
     private String password;

     @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
     private String nom;

     @Size(max = 100, message = "Le prenom ne peut pas dépasser 100 caractères")
     private String prenom;

     private String bio;

     @Column(name="profile_picture_url")
     private String profilePictureUrl;    // chemin vers la photo

    @Column(name = "profile_picture_filename")
    private String profilePictureFilename;


    @CreationTimestamp
    private LocalDateTime createdAt;

     @UpdateTimestamp
     private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();


    public User(Long id, String email, String password, String nom, String prenom, String bio, String profilePictureUrl, String profilePictureFilename, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.profilePictureFilename = profilePictureFilename;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "id:"+id+" , nom:"+nom+" , prenom:"+prenom+" , email:"+email+" , createdAt:"+createdAt+" , bio:"+bio+" .\n";
    }


    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getProfilePictureFilename() {
        return profilePictureFilename;
    }

    public void setProfilePictureFilename(String profilePictureFilename) {
        this.profilePictureFilename = profilePictureFilename;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
