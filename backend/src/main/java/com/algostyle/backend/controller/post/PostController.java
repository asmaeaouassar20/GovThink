package com.algostyle.backend.controller.post;

import com.algostyle.backend.model.dto.post.CommentDTO;
import com.algostyle.backend.model.dto.post.CreateCommentRequest;
import com.algostyle.backend.model.dto.post.CreatePostRequest;
import com.algostyle.backend.model.dto.post.PostDTO;
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
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id){
        try{
            PostDTO postDTO = postService.getPostById(id);
            return ResponseEntity.ok(postDTO);
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







    

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long id){
        try{
            List<CommentDTO> commentDTOS=this.postService.getCommentsByPost(id);
            return ResponseEntity.ok(commentDTOS);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long id,
            @RequestBody CreateCommentRequest request,
            Principal principal
    ){
        try{
            CommentDTO commentDTO = this.postService.addComment(id,request,principal.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(commentDTO);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }


}
