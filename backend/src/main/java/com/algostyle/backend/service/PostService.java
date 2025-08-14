package com.algostyle.backend.service;

import com.algostyle.backend.model.dto.post.*;
import com.algostyle.backend.model.entity.Comment;
import com.algostyle.backend.model.entity.Post;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.repository.CommentRepository;
import com.algostyle.backend.repository.PostRepository;
import com.algostyle.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public List<PostDTO> getAllPosts(){
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }



    public PostDTO createPost(CreatePostRequest request, String email){
        User author = userRepository.findByEmail(email);
        Post post = new Post(request.getContent(),author);

        Post createdPost = this.postRepository.save(post);
        return new PostDTO(createdPost);
    }





    public PostResponse getPostById(Long postId){
        List<Comment> comments = this.commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
        Post post=this.postRepository.findById(postId).orElseThrow(()->new RuntimeException("post avec id "+postId+" est non trouvable"));
        return new PostResponse(post);
    }




    public CommentDTO addComment(Long postId, CreateCommentRequest request, String email){
        Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("post avec id "+postId+" n'existe pas"));
        User author = userRepository.findByEmail(email);
        Comment comment = new Comment(request.getContent(), author, post);
        Comment createdComment=commentRepository.save(comment);
        return new CommentDTO(createdComment);
    }


    public PostDTO addLike(Long postId){
        Post post=this.postRepository.findById(postId).orElseThrow(()-> new RuntimeException("Le post avec id "+postId+" non trouvable"));
        post.setLikesCount(post.getLikesCount()+1);
        Post savedPost=this.postRepository.save(post);
        return new PostDTO(savedPost);
    }



}
