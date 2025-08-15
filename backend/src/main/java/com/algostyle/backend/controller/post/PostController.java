package com.algostyle.backend.controller.post;

import com.algostyle.backend.model.dto.post.*;
import com.algostyle.backend.model.entity.Comment;
import com.algostyle.backend.model.entity.Post;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.service.PostService;
import com.algostyle.backend.service.UserService;
import jakarta.validation.Valid;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

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



    @PostMapping("/{id}/save")
    public ResponseEntity<UserDTO> savePost(
            @PathVariable(value = "id") Long postId,
            @AuthenticationPrincipal User user
    ){
        try{
            UserDTO userDTO=postService.savePost(postId,user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}/unsave")
    public ResponseEntity<UserDTO> unsavePost(
            @PathVariable(value = "id") Long postId,
            @AuthenticationPrincipal User user
    ){
        try{
            UserDTO userDTO = postService.unsavePost(postId,user.getId());
            return ResponseEntity.ok(userDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/saved-posts")
    public ResponseEntity<List<PostDTO>> getSavedPosts(@AuthenticationPrincipal User user){
       try{
           List<PostDTO> savedPosts = userService.getSavedPosts(user.getId());
           return ResponseEntity.ok(savedPosts);
       }catch(Exception e){
           return ResponseEntity.badRequest().body(null);
       }
    }

}
