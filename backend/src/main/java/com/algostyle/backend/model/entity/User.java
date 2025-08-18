package com.algostyle.backend.model.entity;


import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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



    public User() {
    }

    public User(Long id, String email, String password, String nom, String prenom, String bio, String profilePictureUrl, String profilePictureFilename) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.profilePictureFilename = profilePictureFilename;
    }



    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Project> projects = new ArrayList<>();



    @ManyToMany
    @JoinTable(
            name = "user_saved_posts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> savedPosts = new HashSet<>();



    // pour sauvegarder un post
    public void savePost(Post post){
        this.savedPosts.add(post);
    }
    public boolean unsavePost(Post post){
        return this.savedPosts.remove(post);
    }
    public boolean isPostSaved(Post post){
        return this.savedPosts.contains(post);
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



    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;}

    public Set<Post> getSavedPosts() {
        return savedPosts;
    }

    public void setSavedPosts(Set<Post> savedPosts) {
        this.savedPosts = savedPosts;
    }





    @Override
    public String toString() {
        return "id:"+id+" , nom:"+nom+" , prenom:"+prenom+" , email:"+email+" , createdAt:"+createdAt+" , bio:"+bio+" , profilePictureUrl:"+profilePictureUrl + " , profilePictureFilename:"+profilePictureFilename+" .\n";
    }


}
