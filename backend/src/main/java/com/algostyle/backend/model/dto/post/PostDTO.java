package com.algostyle.backend.model.dto.post;

import com.algostyle.backend.model.entity.Post;

import java.time.LocalDateTime;

public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private String authorProfilePictureUrl; // propriété pour l'URL de la photo de profile
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int commentCount;
    private int likesCount;


    public PostDTO(Post post){
        this.id=post.getId();
        this.title=post.getTitle();
        this.content=post.getContent();
        this.authorName=post.getUser().getPrenom()+" "+post.getUser().getNom();
        this.authorProfilePictureUrl="http://localhost:8080"+post.getUser().getProfilePictureUrl();
        this.createdAt=post.getCreatedAt();
        this.updatedAt=post.getUpdatedAt();
        this.commentCount=post.getComments().size();
        this.likesCount=post.getLikesCount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorProfilePictureUrl() {
        return authorProfilePictureUrl;
    }

    public void setAuthorProfilePictureUrl(String authorProfilePictureUrl) {
        this.authorProfilePictureUrl = authorProfilePictureUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likeCount) {
        this.likesCount = likeCount;
    }


}
