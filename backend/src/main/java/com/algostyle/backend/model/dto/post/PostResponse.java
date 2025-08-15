package com.algostyle.backend.model.dto.post;

import com.algostyle.backend.model.entity.Comment;
import com.algostyle.backend.model.entity.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int commentCount;
    private int likesCount;
    private List<CommentDTO> commentsDto = new ArrayList<>();


    public PostResponse(Post post){
        this.id=post.getId();
        this.title=post.getTitle();
        this.content=post.getContent();
        this.authorName=post.getUser().getNom()+" "+post.getUser().getPrenom();
        this.createdAt=post.getCreatedAt();
        this.updatedAt=post.getUpdatedAt();
        this.commentCount=post.getComments().size();
        this.likesCount=post.getLikesCount();
        this.commentsDto=post.getComments().stream().map(CommentDTO::new).collect(Collectors.toList());
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

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public List<CommentDTO> getCommentsDto() {
        return commentsDto;
    }

    public void setCommentsDto(List<CommentDTO> commentsDto) {
        this.commentsDto = commentsDto;
    }
}
