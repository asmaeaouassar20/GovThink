package com.algostyle.backend.unit.service;

import com.algostyle.backend.model.dto.auth.SignupRequest;
import com.algostyle.backend.model.dto.post.PostDTO;
import com.algostyle.backend.model.dto.userprofile.UserProfileDTO;
import com.algostyle.backend.model.entity.Post;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.repository.UserRepository;
import com.algostyle.backend.service.FileStorageService;
import com.algostyle.backend.service.UserService;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


/**
 * La classe UserServiceTest est un test unitaire, car :
 * -> Toutes les dépendances sont mockées (simulées)
 * -> Aucune vraie connexion à la BD
 * -> Aucun vrai service externe est appelé
 * -> Utilisation de mock pour simuler le comportement
 * -> Scope limité : Chaque test vérifie une seule méthode ou un seul scénario
 * -> Focus sur la logique métier de UserService uniquement
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock   // Utilisée pour cérer un objet simulé (fake), Utilisée pour les dépendances
    private UserRepository userRepository;
    @Mock   // Utilisée pour cérer un objet simulé (fake), Utilisée pour les dépendances
    private PasswordEncoder passwordEncoder;
    @Mock
    private FileStorageService fileStorageService;
    @InjectMocks    // Utilisée pour créer l'objet réel à tester, Injecte automatiquement tous les Mock dedans
    private UserService userService;


    private SignupRequest signupRequest;
    private User mockUser;


    @BeforeEach
    void setUp(){
        // Initialiser les données de test
        signupRequest = new SignupRequest();
        signupRequest.setEmail("asmae@gmail.com");
        signupRequest.setPassword("Asmae@123");
        signupRequest.setPasswordconfirm("Asmae@123");

        mockUser=new User();
        mockUser.setPassword("hashedPassword");
        mockUser.setEmail("asmae@gmail.com");
    }


    @Test
    void createUser_WithoutProfilePicture_ShouldSucceed(){

        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        UserProfileDTO userProfileDTO=userService.createUser(signupRequest);

        // Assert
        Assertions.assertNotNull(userProfileDTO);
        verify(userRepository).existsByEmail("asmae@gmail.com");
        verify(passwordEncoder).encode("Asmae@123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WithProfilePicture_ShouldSucceed() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("mdpHashe");
        when(fileStorageService.saveProfilePicture(mockFile)).thenReturn("...filePath");
        when(fileStorageService.createImageUrl("...filePath")).thenReturn("imageUrl");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserProfileDTO userProfileDTO=userService.createUser(signupRequest,mockFile);

        Assertions.assertNotNull(userProfileDTO);
        verify(fileStorageService).saveProfilePicture(any(MultipartFile.class));
        verify(fileStorageService).createImageUrl(anyString());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WithExistingEmail_ShouldThrowException(){

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        RuntimeException exception=Assertions.assertThrows(RuntimeException.class,
                () -> userService.createUser(signupRequest)
                );
        Assertions.assertEquals("Cet email est déjà pris", exception.getMessage());
        verify(userRepository).existsByEmail("asmae@gmail.com");
        verify(userRepository,never()).save(any(User.class));
    }

    @Test
    void createUser_WithPasswordMismatch_shouldThrowException(){
        signupRequest.setPasswordconfirm("mdpasse different");
        RuntimeException exception = Assertions.assertThrows( RuntimeException.class, ()->userService.createUser(signupRequest));
        Assertions.assertEquals(exception.getMessage(),"Problème lors de la confirmation du mot de passe");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_WithProfilePictureUploadFailure_ShouldStillCreateUser() throws Exception{
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("mpdHashe");
        when(fileStorageService.saveProfilePicture(any())).thenThrow(new RuntimeException());
        when(userRepository.save(any(User.class))).thenReturn(mockUser);


        UserProfileDTO userProfileDTO=userService.createUser(signupRequest, mockFile);

        Assertions.assertNotNull(userProfileDTO);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void authenticate_WithValidCredentials_ShouldReturnUser(){
        // Arrange
        String password = "Asmae@123";
        String email="asmae@gmail.com";

        when(userRepository.findByEmail(anyString())).thenReturn(mockUser);
        when(passwordEncoder.matches(password,mockUser.getPassword())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        User user=userService.authenticate(email,password);

        // Assert
        Assertions.assertNotNull(user);
        Assertions.assertEquals(mockUser.getEmail(),user.getEmail());
        verify(userRepository).findByEmail(anyString());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).matches(password,mockUser.getPassword());
    }

    @Test
    void authenticate_WithInvalidPassword_ShouldReturnNull(){
        // Arrange
        String email="asmae@gmail.com";
        String password="wrongPassword";
        when(userRepository.findByEmail(email)).thenReturn(mockUser);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(passwordEncoder.matches(password,mockUser.getPassword())).thenReturn(false);

        // Act
        User user=userService.authenticate(email,password);

        // Assert
        Assertions.assertNull(user);
        verify(passwordEncoder).matches(password,mockUser.getPassword());
    }

    @Test
    void findByEmail_WithExistingEmail_ShouldReturnUser(){
        String existingEmail="asmae@gmail.com";
        when(userRepository.findByEmail(existingEmail)).thenReturn(mockUser);
        User user=userService.findByEmail(existingEmail);
        Assertions.assertNotNull(user);
        verify(userRepository).findByEmail(any(String.class));
        Assertions.assertEquals(existingEmail,mockUser.getEmail());
    }

    @Test
    void findByEmail_WithNonExistentEmail_ShouldReturnNull(){
        // Arrange
        when(userRepository.findByEmail(any(String.class))).thenReturn(null);

        // ACt
        User user=userService.findByEmail("nonexistingemail@gmail.com");

        // Assert
        Assertions.assertNull(user);
        verify(userRepository).findByEmail(any(String.class));
    }

    @Test
    void getSavedPosts_WithValidUserId_ShouldReturnPostList(){
        // Arrange
        Set<Post> savedPosts=new HashSet<>();

        Post post1=new Post();
        Post post2=new Post();
        post1.setUser(mockUser);
        post2.setUser(mockUser);

        savedPosts.add(post1);
        savedPosts.add(post2);

        mockUser.setSavedPosts(savedPosts);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockUser));

        // Act
        List<PostDTO> postDTOList=userService.getSavedPosts(any(Long.class));

        // Assert
        Assertions.assertNotNull(postDTOList);
        Assertions.assertEquals(2,postDTOList.size());
        verify(userRepository).findById(any(Long.class));
    }

    @Test
    void getSavedPosts_WithInvalidUserId_ShouldThrowException(){
        // Arrange
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception=Assertions.assertThrows(RuntimeException.class,
                () -> userService.getSavedPosts(999L)
                );

        Assertions.assertEquals(exception.getMessage(),"user avec id 999 est introuveable");
        Assertions.assertTrue(exception.getMessage().contains("introuveable"));
        verify(userRepository).findById(any(Long.class));
    }

    @Test
    void getSavedPosts_WithEmptySavedPosts_ShouldReturnEmptyList(){
        // Arrange
        Set<Post> savedPosts=new HashSet<>();
        mockUser.setSavedPosts(savedPosts);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mockUser));

        // Act
        List<PostDTO> postDTOList=userService.getSavedPosts(999L);

        // Assert
        Assertions.assertNotNull(postDTOList);
        Assertions.assertTrue(postDTOList.isEmpty());
        Assertions.assertEquals(0,postDTOList.size());
        verify(userRepository).findById(any(Long.class));
    }
}





















