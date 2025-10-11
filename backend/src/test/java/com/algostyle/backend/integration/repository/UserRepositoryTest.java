package com.algostyle.backend.integration.repository;

import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


/**
 *  Cette classe est un test d'intégration, car :
 *  -> Elle utilise @DataJpaTest qui démarre un contexte Spring
 *  -> Interagit avec une vraie BD (H2 en mémoire)
 *  -> Teste l'intégration entre Spring Data JPA et la BD
 */

@DataJpaTest    // Annotation pour tester les repositories JPA
                // Elle configure automatiquement une BD en mémoire H2 pour les tests
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;    // Pour gérer les entités dans les tests

    User user1=new User();
    User savedUser1=new User();


    @BeforeEach
     void addUsersToDB(){
        user1.setEmail("asmae@gmail.com");
        user1.setPassword("Asmae123");
        user1.setNom("Asmae");
        savedUser1 = entityManager.persistAndFlush(user1);
    }

    @Test
    public void testFindByEmail_WhenUserExists_ShouldReturnUser(){
        // Arrange (Préparation)
        // Déjà fait dans la méthode addUsersToDB()

        // Act (Action)
        User found = userRepository.findByEmail("asmae@gmail.com");

        // Assert (Vérification)
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getEmail()).isEqualTo("asmae@gmail.com");
        Assertions.assertThat(found.getNom()).isEqualTo("Asmae");
    }

    @Test
    public void testFindByEmail_WhenUserDoesNotExist_ShouldReturnNull(){
        // Act (Action)
        User user = userRepository.findByEmail("asmae@gmail.com");

        // Assert (Vérification)
        Assertions.assertThat(user).isNotNull();
    }

    @Test
    public void testExistsByEmail_WhenEmailExists_ShouldReturnTrue(){
        // Act
        boolean exists = userRepository.existsByEmail("asmae@gmail.com");

        // Assert
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByEmail_WhenEmailNotExist_ShouldReturnFalse(){
        // Act
        boolean exists = userRepository.existsByEmail("ali@gmail.com");

        // Assert
        Assertions.assertThat(exists).isFalse();
    }


    @Test
    public void testExistsByEmailAndIdNot_WhenEmailExistsForDifferentId_ShouldReturnTrue(){
        // Act
        boolean exist = userRepository.existsByEmailAndIdNot("asmae@gmail.com",2L);

        // Assert
        Assertions.assertThat(exist).isTrue();
    }

    @Test
    public void testExistsByEmailAndIdNot_WhenEmailExistsForSameUser_ShouldReturnFalse(){
        // Act
        boolean exist = userRepository.existsByEmailAndIdNot("asmae@gmail.com",savedUser1.getId());

        // Assertion
        Assertions.assertThat(exist).isFalse();
    }

}