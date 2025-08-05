package com.algostyle.backend.model.dto.userprofile;

import jakarta.persistence.Column;

public class UserProfileDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;

    private String bio;

    private String profilePictureUrl;

    private String profilePictureFilename;

    public UserProfileDTO(Long id, String nom, String prenom, String email, String bio, String profilePictureUrl, String profilePictureFilename) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.profilePictureFilename = profilePictureFilename;
    }

    @Override
    public String toString(){
        return "id:"+id+" , nom:"+nom+" , prenom:"+prenom+" , email:"+email+" , bio:"+bio +" . \n";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
