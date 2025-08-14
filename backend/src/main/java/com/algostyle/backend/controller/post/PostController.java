package com.algostyle.backend.controller.post;

import com.algostyle.backend.model.dto.post.*;
import com.algostyle.backend.model.entity.Comment;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(){
        List<PostDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id){
        try{
            PostResponse postResponse = postService.getPostById(id);
            return ResponseEntity.ok(postResponse);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<PostDTO> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal User user
            ){
        try{
            PostDTO postDTO=postService.createPost(request,user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(postDTO);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }





    

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable(value = "id") Long postId,
            @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal User user
    ){

        try{
            CommentDTO commentDTO = this.postService.addComment(postId,request,user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(commentDTO);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }



    @PostMapping("/{id}/likes")
    public ResponseEntity<PostDTO> addLike(@PathVariable(value = "id") Long postId){
        try{
            PostDTO postDTO=this.postService.addLike(postId);
            return ResponseEntity.status(HttpStatus.CREATED).body(postDTO);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }












}
