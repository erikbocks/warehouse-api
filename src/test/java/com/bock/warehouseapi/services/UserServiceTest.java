package com.bock.warehouseapi.services;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.UserRole;
import com.bock.warehouseapi.entities.dtos.UserPasswordDTO;
import com.bock.warehouseapi.entities.dtos.UserUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.repositories.UserRepository;
import com.bock.warehouseapi.services.impls.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    UserServiceImpl service;

    User fakeUser;
    UserUpdateDTO fakeUpdateDTO;

    @BeforeEach
    void setup() {
        BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
        fakeUser = new User(1, "user", "user@email.com", passEncoder.encode("Senha_forte123#"), UserRole.ADMIN);
        fakeUpdateDTO = new UserUpdateDTO("fakeTest", "fake@email.com");
    }

    @Test
    @DisplayName("Should retrieve successfully the user by its id.")
    void findById_CaseSuccess() throws InvalidDataException {
        Integer id = 1;
        when(repository.findById(anyInt())).thenReturn(Optional.of(fakeUser));

        User dbUser = service.findById(id);

        verify(repository, times(1)).findById(anyInt());

        assertThat(dbUser).isNotNull();
    }

    @DisplayName("Should throw exception because of invalid id.")
    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {0})
    void findById_CaseInvalidId_ThrowsException(Integer id) {
        Exception thrown = assertThrows(InvalidDataException.class, () -> {
            service.findById(id);
        });

        verifyNoInteractions(repository);

        assertEquals("O campo ID não pode ser nulo e nem igual a zero.", thrown.getMessage());
    }

    @Test
    @DisplayName("Should retrive succefully the user by its username.")
    void findByUsername_CaseSuccess() throws InvalidDataException {
        String username = "username";
        when(repository.findByUsername(anyString())).thenReturn(Optional.of(fakeUser));

        User dbUser = service.findByUsername(username);

        verify(repository, times(1)).findByUsername(anyString());

        assertThat(dbUser).isNotNull();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "")
    @DisplayName("Should throw exception because of invalid username.")
    void findByUsername_CaseInvalidUsername_ThrowsException(String username) {
        Exception thrown = assertThrows(InvalidDataException.class, () -> {
            service.findByUsername(username);
        });

        verifyNoInteractions(repository);

        assertEquals("O nome de usuário não pode ser nulo ou vazio.", thrown.getMessage());
    }

    @Test
    @DisplayName("Should successfully update user.")
    void updateUser_CaseSuccess() throws InvalidDataException {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.findByUsername(anyString())).thenReturn(Optional.empty());

        service.updateUser(fakeUser, fakeUpdateDTO);
        verify(repository, times(1)).findByEmail(anyString());
        verify(repository, times(1)).findByUsername(anyString());
        verify(repository, times(1)).saveAndFlush(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception because of not unique email.")
    void updateUser_CaseNotUniqueEmail_ThrowsException() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(fakeUser));

        Exception thrown = assertThrows(InvalidDataException.class, () -> {
            service.updateUser(fakeUser, fakeUpdateDTO);
        });

        verify(repository, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(repository);

        assertEquals("Esse email já está cadastrado.", thrown.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because of not unique username.")
    void updateUser_CaseNotUniqueUsername_ThrowsException() {
        when(repository.findByUsername(anyString())).thenReturn(Optional.of(fakeUser));

        Exception thrown = assertThrows(InvalidDataException.class, () -> {
            service.updateUser(fakeUser, fakeUpdateDTO);
        });

        verify(repository, times(1)).findByEmail(anyString());
        verify(repository, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(repository);

        assertEquals("Esse nome de usuário já está cadastrado.", thrown.getMessage());
    }

    @Test
    @DisplayName("Should successfullwy update user password.")
    void updatePassword_CaseSuccess() throws InvalidDataException {
        UserPasswordDTO toUpdatePasswords = new UserPasswordDTO("Senha_forte123#", "Senha.Mais.forte123#");

        service.updatePassword(toUpdatePasswords, fakeUser);
        verify(repository, times(1)).saveAndFlush(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception because of equal old and new password.")
    void updatePassword_CaseEqualPassword_ThrowsException() {
        UserPasswordDTO toUpdatePasswords = new UserPasswordDTO("Senha_forte123#", "Senha_forte123#");

        Exception thrown = assertThrows(InvalidDataException.class, () -> {
            service.updatePassword(toUpdatePasswords, fakeUser);
        });

        verifyNoInteractions(repository);

        assertEquals("A senha nova não pode ser igual a já cadastrada no banco.", thrown.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because of wrong old password.")
    void updatePassword_CaseNotEqualsToDb_ThrowsException() {
        UserPasswordDTO toUpdatePasswords = new UserPasswordDTO("Senha.errada1#", "Senha.Mais.forte123#");

        Exception thrown = assertThrows(InvalidDataException.class, () -> {
            service.updatePassword(toUpdatePasswords, fakeUser);
        });

        verifyNoInteractions(repository);

        assertEquals("A senha atual não coincide com a cadastrada.", thrown.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because of invalid new password.")
    void updatePassword_CaseInvalidPassword_ThrowsException() {
        UserPasswordDTO toUpdatePasswords = new UserPasswordDTO("Senha_forte123#", "wrong");

        Exception thrown = assertThrows(InvalidDataException.class, () -> {
            service.updatePassword(toUpdatePasswords, fakeUser);
        });
        verify(repository, times(0)).saveAndFlush(any(User.class));

        assertEquals("Sua senha deve conter pelo menos um número, uma letra maiúscula, um simbolo e ter de 6 a 30 caracteres", thrown.getMessage());
    }

    @Test
    @DisplayName("Should remove user successfully")
    void removeUser_CaseSuccess() throws InvalidDataException {
        Integer id = 1;

        service.removeUser(id);
        verify(repository, times(1)).deleteById(anyInt());
    }

    @ParameterizedTest
    @DisplayName("Should throw exception because of invalid id")
    @NullSource
    @ValueSource(ints = {0})
    void removeUser_CaseInvalidId_ThrowsException(Integer id) {

        Exception thrown = assertThrows(InvalidDataException.class, () -> {
            service.removeUser(id);
        });

        verifyNoInteractions(repository);

        assertEquals("O campo ID não pode ser nulo e nem igual a zero.", thrown.getMessage());
    }
}