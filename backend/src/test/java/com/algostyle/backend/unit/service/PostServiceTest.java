package com.algostyle.backend.unit.service;

import com.algostyle.backend.model.dto.post.*;
import com.algostyle.backend.model.entity.Comment;
import com.algostyle.backend.model.entity.Post;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.repository.CommentRepository;
import com.algostyle.backend.repository.PostRepository;
import com.algostyle.backend.repository.UserRepository;
import com.algostyle.backend.service.PostService;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @Mock
    CommentRepository commentRepository;
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

        // le post sauvegardé
        Post savedPost = new Post();
        savedPost.setId(1L);
        savedPost.setContent("content test");
        savedPost.setTitle("title test");
        savedPost.setUser(author);

        // le post créé
        CreatePostRequest createPostRequest=new CreatePostRequest();
        createPostRequest.setTitle("title test");
        createPostRequest.setContent("content test");


        // *** Mocking call ***
        when(userRepository.findByEmail(email)).thenReturn(author);
        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenReturn(savedPost);

        // *** Calling actual method ***
        PostDTO addedPostDTO=postService.createPost(createPostRequest,email);

        // *** Assertions ***
        Assertions.assertNotNull(addedPostDTO);
        Assertions.assertEquals(savedPost.getId(), addedPostDTO.getId());
        Assertions.assertEquals(savedPost.getTitle(),addedPostDTO.getTitle());
        Assertions.assertEquals(savedPost.getContent(),addedPostDTO.getContent());

        // Vérifier que les bonnes méthodes ont été appelées
        verify(userRepository, times(1)).findByEmail(email);
        verify(postRepository, times(1)).save(any(Post.class));
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


    @Test
    public void getAllPostsShouldReturnAllPostsOrderedByCreatedAtDesc(){
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Premier post");
        post1.setContent("Contenu 1");
        post1.setUser(user1);

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Deuxième post");
        post2.setContent("Contenu 2");
        post2.setUser(user2);

        List<Post> posts = Arrays.asList(post1, post2);

        when(postRepository.findAllByOrderByCreatedAtDesc()).thenReturn(posts);

        // Act
        List<PostDTO> result = postService.getAllPosts();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Premier post", result.get(0).getTitle());
        Assertions.assertEquals("Deuxième post", result.get(1).getTitle());

        verify(postRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    public void getPostByIdShouldReturnPostWithComments(){
        // Arrange
        User author = new User();
        author.setId(1L);

        post.setId(1L);
        post.setUser(author);

        Comment comment1=new Comment();
        comment1.setId(1L);
        comment1.setPost(post);
        comment1.setContent("commentaire 1");
        comment1.setUser(author);

        Comment comment2=new Comment();
        comment2.setId(1L);
        comment2.setPost(post);
        comment2.setContent("commentaire 2");
        comment2.setUser(author);
        List<Comment> comments=Arrays.asList(comment1, comment2);

        when(commentRepository.findByPostIdOrderByCreatedAtDesc(1L)).thenReturn(comments);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Act
        PostResponse postResponse=  postService.getPostById(post.getId());

        // Assert
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(2,postResponse.getCommentCount());
        verify(postRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).findByPostIdOrderByCreatedAtDesc(1L);
    }

    @Test
    public void getPostByIdShouldThrowExceptionWhenPostNotFound(){
        // Arrange
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,()->{
            postService.getPostById(1L);
        });
        Assertions.assertTrue(exception.getMessage().contains("post avec id 1 est non trouvable"));
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    public void addCommentShouldAddCommentSuccessfully(){
        // Arrange
        String email = "asmae@gmail.com";

        User author = new User();
        author.setId(1L);
        author.setEmail(email);

        post.setId(1L);

        CreateCommentRequest createCommentRequest=new CreateCommentRequest();
        createCommentRequest.setContent("test");

        Comment comment=new Comment(createCommentRequest.getContent(),author,post);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail(email)).thenReturn(author);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        CommentDTO commentDTO=postService.addComment(post.getId(),createCommentRequest,email);

        // Assert
        Assertions.assertNotNull(commentDTO);
        Assertions.assertEquals(commentDTO.getContent(),"test");
        verify(postRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByEmail(email);
        verify(commentRepository, times(1)).save(any(Comment.class));

    }

    @Test
    public void addCommentShouldThrowExceptionWhenPostNotFound(){
        // Arrange
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, ()->{
            postService.addComment(1L,new CreateCommentRequest(),"some email");
        });
        Assertions.assertTrue(exception.getMessage().contains("post avec id 1 n'existe pas"));
    }


    @Test
    public void addLikeShouldIncrementLikesCount(){
        // Arrange
        User user=new User();

        post.setId(1L);
        post.setLikesCount(7);
        post.setUser(user);

        Post updatedPost = new Post();
        updatedPost.setId(1L);
        updatedPost.setUser(user);
        updatedPost.setLikesCount(8);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(updatedPost);

        // Act
        PostDTO postDTO=postService.addLike(1L);

        // Assert
        Assertions.assertNotNull(postDTO);
        Assertions.assertEquals(post.getLikesCount(),8);
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(post);
    }


    @Test
    public void addLikeShouldThrowExceptionWhenPostNotFound(){
        // Arrange
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () ->  postService.addLike(1L)
        );
        Assertions.assertTrue(exception.getMessage().contains("Le post avec id 1 non trouvable"));
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, never()).save(any(Post.class));
    }


    @Test
    public void savePostShouldSavePostSuccessfully(){
        // Arrange
        User user=new User();
        user.setId(1L);
        // S'assurer que le poste n'est déjà sauvegardé
        user.setSavedPosts(new HashSet<>());

        post.setId(1L);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        UserDTO userDTO=postService.savePost(1L,1L);

        // Assert
        Assertions.assertNotNull(userDTO);
        Assertions.assertTrue(user.getSavedPosts().contains(post));
        verify(postRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }


    @Test
    public void savePostShouldThrowExceptionWhenUserNotFound(){
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> postService.savePost(1L,1L)
                );
        Assertions.assertTrue(exception.getMessage().contains("user avec id 1 est non trouvable"));
        verify(userRepository, times(1)).findById(1L);
        verify(postRepository, never()).findById(any());
        verify(userRepository, times(0)).save(any(User.class));
    }


    @Test
    public void savePostShouldThrowExceptionWhenPostNotFound(){
        // Arrange
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                ()-> {postService.savePost(1L,1L);}
                );
        Assertions.assertTrue(exception.getMessage().contains("post avec id 1 est non trouvable"));
        verify(userRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void savePostShouldThrowExceptionWhenPostIsAlreadySaved() {
        // Arrange
        post.setId(1L);

        User user = new User();
        user.setId(1L);
        Set<Post> savedPosts = new HashSet<>();
        savedPosts.add(post);
        user.setSavedPosts(savedPosts);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Act & Assert
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> postService.savePost(1L, 1L)
        );
        Assertions.assertTrue(exception.getMessage().contains("Post déjà sauvegardé"));
        verify(userRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(user);
    }


    @Test
    public void unsavePostShouldUnsavePostSuccessfully(){
        // Arrange
        User user=new User();
        user.setId(1L);

        post.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        UserDTO userDTO=postService.unsavePost(1L,1L);

        // Assert
        Assertions.assertNotNull(userDTO);
        verify(userRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        Assertions.assertFalse(user.getSavedPosts().contains(post));
    }

    @Test
    public void unsavePostShouldThrowExceptionWhenUserNotFound(){
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception  = Assertions.assertThrows(RuntimeException.class,
                () -> postService.unsavePost(1L,1L)
                );
        Assertions.assertTrue(exception.getMessage().contains("user avec id 1 non trouvable"));
        verify(userRepository, times(1)).findById(1L);
        verify(postRepository, never()).findById(1L);
    }


    @Test
    public void getPostByUserShouldReturnUserPosts(){
        // Arrange
        User user=new User();
        user.setId(1L);

        Post post1=new Post();
        post1.setId(1L);
        post1.setTitle("post1");
        post1.setUser(user);
        Post post2=new Post();
        post2.setId(2L);
        post2.setTitle("post2");
        post2.setUser(user);
        Post post3=new Post();
        post3.setTitle("post3");
        post3.setId(3L);
        post3.setUser(user);

        List<Post> posts=Arrays.asList(post1, post2, post3);

        when(postRepository.findAllByUser(user)).thenReturn(posts);

        // Act
        List<PostDTO> postDTOS=postService.getPostByUser(user);

        // Assert
        Assertions.assertNotNull(postDTOS);
        Assertions.assertEquals(3,postDTOS.size());
        Assertions.assertEquals("post1",postDTOS.get(0).getTitle());
        Assertions.assertEquals("post2",postDTOS.get(1).getTitle());
        Assertions.assertEquals("post3",postDTOS.get(2).getTitle());
        verify(postRepository, times(1)).findAllByUser(user);
    }


    @Test
    public void deletePostByIdShouldThrowExceptionWhenUserNotAuthorized(){
        // Arrange
        User owner=new User();
        owner.setId(1L);

        User otherUser=new User();
        otherUser.setId(2L);

        post.setId(1L);
        post.setUser(owner);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Act & Asser
        RuntimeException exception=Assertions.assertThrows(RuntimeException.class,
                ()->{postService.deletePostById(1L,otherUser);}
                );
        Assertions.assertTrue(exception.getMessage().contains("Vous n'avez pas le droit de supprimer ce post"));
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, never()).deleteById(1L);
    }


    @Test
    public void deletePostByIdShouldDoNothingWhenPostNotFound(){
        // Arrange
        User user=new User();
        user.setId(1L);

        when(postRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act
        postService.deletePostById(1L,user);

        // Assert
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, never()).deleteById(any(Long.class));
    }

}
