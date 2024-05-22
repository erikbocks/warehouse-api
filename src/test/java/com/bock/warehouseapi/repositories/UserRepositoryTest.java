package com.bock.warehouseapi.repositories;

import com.bock.warehouseapi.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    @DisplayName("Should find user by username successfully.")
    void findByUsernameCaseSuccess() {
        String username = "robertinho";
        Optional<User> result = repository.findByUsername(username);

        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not find user by username.")
    void findByUsernameCaseFailure() {
        String username = "robertinho_2";
        Optional<User> result = repository.findByUsername(username);

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should find user by email successfully.")
    void findByEmailCaseSuccess() {
        String email = "robertinho@gmail.com";
        Optional<User> result = repository.findByEmail(email);

        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not find user by email.")
    void findByEmailCaseFailure() {
        String email = "robertinho1@gmail.com";
        Optional<User> result = repository.findByEmail(email);

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();
    }


    @ParameterizedTest
    @ValueSource(strings = {"robertinho@gmail.com", "robertinho"})
    @DisplayName("Should find user by email and also by username")
    void findBySubjectCaseSuccess(String subject) {
        UserDetails result = repository.findBySubject(subject);

        assertThat(result).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"robertinho1@gmail.com", "robertinho1"})
    @DisplayName("Should not find user by email and neither by username")
    void findBySubjectCaseFailure(String subject) {
        UserDetails result = repository.findBySubject(subject);

        assertThat(result).isNull();
    }


    @Test
    @DisplayName("Should find user by id.")
    void findBySubjectIdCaseSuccess() {
        Integer id = 1;
        UserDetails result = repository.findBySubjectId(id);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should not find user by id.")
    void findBySubjectIdCaseFailure() {
        Integer id = 999;
        UserDetails result = repository.findBySubjectId(id);

        assertThat(result).isNull();
    }
}