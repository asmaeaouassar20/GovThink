package com.algostyle.backend.integration;

import com.algostyle.backend.model.dto.post.CreateCommentRequest;
import com.algostyle.backend.model.dto.post.CreatePostRequest;
import com.algostyle.backend.model.dto.post.PostDTO;
import com.algostyle.backend.model.entity.Post;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.repository.PostRepository;
import com.algostyle.backend.repository.UserRepository;
import com.algostyle.backend.utils.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Configuration des annotations de test Spring Boot
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)    // Démarre l'application Spring Boot avec un port aléatoire
@TestPropertySource(locations = "classpath:application-test.properties")    //charge le fichier de propriétés de test
class PostControllerIntegrationTest {

    // Injection des dépendances nécessaires pour les tests
    @Autowired
    private TestRestTemplate testRestTemplate; // Client HTTP pour tester les endpoints REST

    @Autowired
    private PostRepository postRepository;  // Repository pour interagir avec la table des posts

    @Autowired
    private UserRepository userRepository; // Repository pour interagir avec la table des utilisateurs



    @Autowired
    private PasswordEncoder passwordEncoder; // Encodeur de mots de passe

    @Autowired
    private JwtUtil jwtUtil;




    @LocalServerPort
    private int port;  // Récupérer le port aléatoire assigné à l'application








    // Variables pour stocker les données de test
    private String baseUrl;
    private User testUser;
    private String jwtToken;








    @BeforeEach // méthode exécutée avant chaque test
    void setUp(){
        // Nettoyage explicite des données
        postRepository.deleteAll();
        userRepository.deleteAll();

        // Construction de l'URL de base pour les endpoints des posts
        baseUrl = "http://localhost:"+port+"/api/posts";

        // Création d'un utilisateur de test
        testUser=new User();
        testUser.setEmail("asmae@gmail.com");
        testUser.setNom("Aouassar");
        testUser.setPrenom("Asmae");
        testUser.setPassword(passwordEncoder.encode("Asmae@123")); // Encodage du mdp
        testUser = userRepository.save(testUser); // sauvegarder l'utilisateur en BD
        jwtToken=jwtUtil.generateToken(testUser.getEmail());
    }





    // Test pour vérifier la récupération de tous les posts
    @Test
    void shouldGetAllPosts(){
        // Arrange - Créer des posts de test
        // Création de 2 posts pour "testUser"
        createTestPost("titre 1", "contenu 1");
        createTestPost("titre 2", "contenu 2");

        // Act
        ResponseEntity<List<PostDTO>> response = testRestTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PostDTO>>() {}
        );

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }



    // Test pour vérifier la récupération d'un post par son ID
    @Test
    void shouldGetPostById(){
        // Arrange, Given  - Création d'un post de test
       Post post = createTestPost("Test Post", "Test Content");


        // When - Exécution de la requête HTTP GET avec l'ID du post
        ResponseEntity<Map> response = testRestTemplate.getForEntity(
                baseUrl + "/" + post.getId(), Map.class);

        // Then - Vérification des résultats
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); // Vérifie que le statut est 200 OK
        assertThat(response.getBody()).containsEntry("title", "Test Post"); // Vérifie le titre
        assertThat(response.getBody()).containsEntry("content", "Test Content"); // Vérifie le contenu
    }



    // Test pour vérifier la gestion d'un post non existant
    @Test
    void shouldReturnNotFoundForNonExistantPost(){
        // When - Tentative de récupération d'un post avec un ID qui n'existe
        ResponseEntity<Map> response = testRestTemplate.getForEntity(baseUrl+"/900",Map.class);

        // Then - Vérification que la réponse est 404 Not
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }




    // Test pour vérifier la création d'un post avec authentification
    @Test
    void shouldCreatePostWhenAuthenticated(){
        // Given - Préparation de la requête de création
        CreatePostRequest request = new CreatePostRequest();
        request.setTitle("Nouveau Post");
        request.setContent("Contenu du nouveau post");

        // Ajouter du token JWt dans les en-têtes pour l'authentification
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<CreatePostRequest> entity = new HttpEntity<>(request, headers);

        // When - Exécution de la requête HTTP POST
        ResponseEntity<Map> response = testRestTemplate.postForEntity(baseUrl,entity,Map.class);

        // Then - Vérification des résultats
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).containsEntry("title","Nouveau Post");
        assertThat(response.getBody()).containsEntry("content","Contenu du nouveau post");

        // Vérification en BD que le post a bien été bien sauvegardé
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getTitle()).isEqualTo("Nouveau Post");
    }



    // Test pour vérifier qu'on ne peut pas créer un post sans authentification
    @Test
    void shouldFailToCreatePostWithoutAuthentification(){
        // Given - Préparation de la requête sans en-têtes d'authentification
        CreatePostRequest createPostRequest=new CreatePostRequest();
        createPostRequest.setTitle("test title");
        createPostRequest.setContent("test content");
/*
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreatePostRequest> entity=new HttpEntity<>(createPostRequest,httpHeaders);
*/
        // When - Exécution de la requête HTTP POST sans token
        ResponseEntity<String> response=testRestTemplate.postForEntity(baseUrl,createPostRequest,String.class);

        // Then - Vérifier que la réponse est 401 Unauthorized
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }




    // Test pour vérifier l'ajout d'un commentaire à un post
    @Test
    void shouldAddCommentToPost(){
        // Given -Création d'un post et préparation du commentaire
        Post post = createTestPost("Post avec commentaire","Contenu du post");
        CreateCommentRequest commentRequest=new CreateCommentRequest();
        commentRequest.setContent("Super commentaire !");

        // Ajout du token JWT dans les en-têtes
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken);
        HttpEntity<CreateCommentRequest> entity=new HttpEntity<>(commentRequest,httpHeaders);

        // When - Exécution de la requête HTTP POST pour ajouter un commentaire
        ResponseEntity<Map> response=testRestTemplate.postForEntity(
                baseUrl+"/"+post.getId()+"/comments",entity,Map.class);

        // Then - Vérification des résultats
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).containsEntry("content","Super commentaire !");
        assertThat(response.getBody()).containsEntry("authorName","Asmae Aouassar");
    }






    // Test pour vérifier l'ajout d'un like à un post
    @Test
    void shouldAddLikeToPost(){
        // Given - Création d'un post
        Post post=createTestPost("test title post", "test content post");

        // When - Exécution de la requête HTTP POST pour ajouter un like
        ResponseEntity<Map> response = testRestTemplate.postForEntity(baseUrl+"/"+post.getId()+"/likes",null, Map.class);

        // Then - Vérification des résultats
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).containsEntry("title","test title post");
        assertThat(response.getBody()).containsEntry("content","test content post");
        assertThat(response.getBody()).containsEntry("authorName","Asmae Aouassar");
        assertThat(response.getBody()).containsEntry("likesCount",1);

        // Vérifier en base de données que le compteur de likes a été incrémenté
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertThat(updatedPost.getLikesCount()).isEqualTo(1);
    }





    // Test pour vérifier la sauvegarde et la désauvegarde d'un post
    @Test
    void shouldSavePost(){
        // Given - Création d'un post
        Post post=createTestPost("test title post","test content post");

        // Ajout du token dans les en-têtes
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken);
        HttpEntity<Void> entity=new HttpEntity<>(httpHeaders);

        // When - Sauvegarde du poste
        ResponseEntity<Map> userDTOResponseEntity=testRestTemplate.postForEntity(
                baseUrl+"/"+ post.getId()+"/save",entity,Map.class
        );

        // Then - Vérification que la sauvegarde a réussi
        assertThat(userDTOResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(userDTOResponseEntity.getBody()).containsEntry("nom","Aouassar");
        assertThat(userDTOResponseEntity.getBody()).containsEntry("prenom","Asmae");
        assertThat(userDTOResponseEntity.getBody()).containsEntry("email","asmae@gmail.com");


        // When - Désauvegarde du post
        ResponseEntity<Map> unsavePostResult = testRestTemplate.exchange(
                baseUrl+"/"+post.getId()+"/unsave",HttpMethod.DELETE,entity, Map.class
        );
        assertThat(unsavePostResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(unsavePostResult.getBody()).containsEntry("nom","Aouassar");
        assertThat(unsavePostResult.getBody()).containsEntry("prenom","Asmae");
        assertThat(unsavePostResult.getBody()).containsEntry("email","asmae@gmail.com");

    }




    // Test pour vérifier la récupération des posts sauvegardés
    @Test
    void shouldGetSavedPosts(){
        // Given - Préparation des en-têtes avec le token
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken);
        HttpEntity<Void> entity=new HttpEntity<>(httpHeaders);

        // When - Exécution de la requêtes HTTP GET pour récupérer les posts sauvegardés
        ResponseEntity<List> savedPosts=testRestTemplate.exchange(
                baseUrl+"/saved-posts", HttpMethod.GET,entity, List.class
        );

        // Then - Vérification des résultats
        assertThat(savedPosts.getStatusCode()).isEqualTo(HttpStatus.OK); // Vérifier que le statut est 200OK
        assertThat(savedPosts.getBody()).isNotNull(); // Vérifier que la réponse n'est pas nulle
    }





    // Test pour vérifier la récupération des posts de l'utilisateur connecté
    @Test
    void shouldGetMyPosts(){
        // Given -Création d'un post par l'utilisateur de test
        createTestPost("test title", "test content");

        // Ajout du token JWT dans
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken);
        HttpEntity<Void> entity=new HttpEntity<>(httpHeaders);

        // When - Exécution de la requête HTTP HET pour récupérer les posts de l'utilisateur
        ResponseEntity<List> response = testRestTemplate.exchange(
                baseUrl+"/my-posts", HttpMethod.GET,entity,List.class
        );

        // Then - Vérification des résultats
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()).isNotNull();
    }



    // Test pour vérifier la suppression d'un post
    @Test
    void shouldDeletePostById(){
        // Given - Création d'un post
        Post postToDelete = createTestPost("test title","test content");

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken);
        HttpEntity<Void> entity=new HttpEntity<>(httpHeaders);

        // When - Exécution de la requête HTTP DELETE pour supprimer le poste
        ResponseEntity<Void> response = testRestTemplate.exchange(
                baseUrl+"/"+postToDelete.getId(), HttpMethod.DELETE,entity, Void.class
        );

        // Then - Vérification des résultats
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Vérifier que le poste est supprimé de la base de données
        assertThat(postRepository.findById(postToDelete.getId())).isEmpty();
        assertThat(postRepository.findAll()).hasSize(0);
    }








    // Méthode utilitaire pour créer un post de test
    private Post createTestPost(String title, String content){
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(testUser);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.flush();
        return postRepository.save(post);
    }


}