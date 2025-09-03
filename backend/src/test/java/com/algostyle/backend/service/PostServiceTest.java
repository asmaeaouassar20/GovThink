package com.algostyle.backend.service;

import com.algostyle.backend.model.dto.post.CreatePostRequest;
import com.algostyle.backend.model.dto.post.PostDTO;
import com.algostyle.backend.model.dto.post.PostResponse;
import com.algostyle.backend.model.entity.Post;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.repository.PostRepository;
import com.algostyle.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest
{

    @Mock
    PostRepository postRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    PostService postService;


    @Test
    void createPostShouldCreatePostSuccessfully(){

        // *** Data Preparation ***
        // auteur du post
        String email="asmae@gmail.com";
        User author=new User();
        author.setId(1L);
        author.setEmail(email);
        // le post
        Post post=new Post();
        post.setId(1L);
        post.setContent("content test");
        post.setTitle("title test");
        post.setUser(author);
        CreatePostRequest createPostRequest=new CreatePostRequest();
        createPostRequest.setTitle(post.getTitle());
        createPostRequest.setContent(post.getContent());


        // *** Mocking call ***
        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenReturn(post);

        // *** Calling actual method ***
        PostDTO addedPostDTO=postService.createPost(createPostRequest,email);

        // *** Assertions ***
        Assertions.assertNotNull(addedPostDTO);
        Assertions.assertEquals(post.getId(), addedPostDTO.getId());
        Assertions.assertEquals(post.getTitle(),addedPostDTO.getTitle());
        Assertions.assertEquals(post.getContent(),addedPostDTO.getContent());
    }

   @Test
    public void deletePostShouldDeletePostSuccessfully(){
        User auteur=new User();
        auteur.setId(1L);

        Post post=new Post();
        post.setId(1L);
        post.setUser(auteur);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        doNothing().when(postRepository).deleteById(1L);


        postService.deletePostById(1L,auteur);

        verify(postRepository, times(1)).deleteById(1L);
        verify(postRepository, times(1)).deleteById(1L);
   }

   @Test
    public void testPrivateMethod_utilisateurAutorise() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Récupérer la méthode privée avec les bons paramètres
        Method utilisateurAutorise = PostService.class.getDeclaredMethod("utilisateurAutorise", Post.class, User.class);

        // Rendre la méthode accessible
        utilisateurAutorise.setAccessible(true);

        // Préparer les objets
        User user=new User();
        user.setId(1L);

        Post post=new Post();
        post.setUser(user);

        // Appeler la méthode rivée
        Boolean resultat = (Boolean) utilisateurAutorise.invoke(postService,post, user);

        // Vérifier le résultat
        Assertions.assertTrue(resultat);
   }

}
