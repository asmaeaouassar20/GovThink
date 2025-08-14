package com.algostyle.backend.service;

import com.algostyle.backend.model.dto.post.CommentDTO;
import com.algostyle.backend.model.dto.post.CreateCommentRequest;
import com.algostyle.backend.model.dto.post.CreatePostRequest;
import com.algostyle.backend.model.dto.post.PostDTO;
import com.algostyle.backend.model.entity.Comment;
import com.algostyle.backend.model.entity.Post;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.repository.CommentRepository;
import com.algostyle.backend.repository.PostRepository;
import com.algostyle.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    public PostDTO getPostById(Long id){
        Post post = this.postRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Post avec id "+id +" non trouvable"));
        return new PostDTO(post);
    }

    public PostDTO createPost(CreatePostRequest request, String email){
        User author = userRepository.findByEmail(email);
        Post post = new Post(request.getContent(),author);

        Post createdPost = this.postRepository.save(post);
        return new PostDTO(createdPost);
    }





    public List<Comment> getCommentsByPostId(Long postId){
        return this.commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
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
        System.out.println(">> post : "+post);
        post.setLikesCount(post.getLikesCount()+1);
        Post savedPost=this.postRepository.save(post);
        return new PostDTO(savedPost);
    }
}
