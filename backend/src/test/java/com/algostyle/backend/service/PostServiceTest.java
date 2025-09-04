package com.algostyle.backend.service;

import com.algostyle.backend.model.dto.post.CreatePostRequest;
import com.algostyle.backend.model.dto.post.PostDTO;
import com.algostyle.backend.model.dto.post.PostResponse;
import com.algostyle.backend.model.dto.post.UserDTO;
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

import javax.swing.text.html.Option;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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

    private static Post  post=null;

    @BeforeAll
    public static void init(){
        post=new Post();
    }

    @Test
    void createPostShouldCreatePostSuccessfully(){

        // *** Data Preparation ***
        // auteur du post
        String email="asmae@gmail.com";
        User author=new User();
        author.setId(1L);
        author.setEmail(email);
        // le post
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

        post.setUser(user);

        // Appeler la méthode privée
        Boolean resultat = (Boolean) utilisateurAutorise.invoke(postService,post, user);

        // Vérifier le résultat
        Assertions.assertTrue(resultat);
   }



   // Tester le cas négatif
    @Test
    public void testPrivateMethod_utilisateurAutoriseSiUtilisateurNonAutorise() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Récupérer la méthode privée avec les bons paramètres
        Method utilisateurAutorise = PostService.class.getDeclaredMethod("utilisateurAutorise", Post.class, User.class);

        // Rendre la méthode accessible
        utilisateurAutorise.setAccessible(true);

        // Préparer les objets
        User user1=new User();
        user1.setId(1L);
        User user2=new User();
        user2.setId(2L);

        post.setUser(user2);

        // Appeler la méthode rivée
        Boolean resultat = (Boolean) utilisateurAutorise.invoke(postService,post, user1);

        // Vérifier le résultat
        Assertions.assertFalse(resultat);
    }


    // lancer une exception lorsque l'utilisateur sauvegarde un post déjà sauvegardé
    @Test
    public void savePostShouldThrowExceptionIfPostIsAlreadySaved(){
        // Arrange (Préparation)a
        User user=new User();

        // On simule que l'utilisateur a déjà sauvegardé ce post
        // C'est la condition qui devrait déclencher l'exception
        user.savePost(post);

        // Configurer les mocks pour qu'ils retournent nos objets de test
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Act & Assert (Action et Vérification)
        RuntimeException exception=Assertions.assertThrows(RuntimeException.class, ()-> {
            postService.savePost(1L,1L);
        });

        // On vérifie que le message de l'exception est exactement celui qu'on attend
        Assertions.assertEquals("Post déjà sauvegardé", exception.getMessage());
        // verify(userRepository,times(0)).save(any(User.class));     // ne sera pas appelée car elle a lancé une exception
        verify(userRepository,never()).save(any(User.class));     // ne sera pas appelée car elle a lancé une exception
    }


}
