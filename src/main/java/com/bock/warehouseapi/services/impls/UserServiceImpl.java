package com.bock.warehouseapi.services.impls;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.UserPasswordDTO;
import com.bock.warehouseapi.entities.dtos.UserUpdateDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.repositories.UserRepository;
import com.bock.warehouseapi.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    private List<String> validateRegexUpdate(String email, String username) {
        List<String> messages = new ArrayList<>();

        Pattern emailRegex = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        Pattern usernameRegex = Pattern.compile("(?=[a-zA-Z0-9._]{6,20}$)(?!.*[_.]{2})[^_.].*[^_.]");

        if (!emailRegex.matcher(email).matches()) {
            messages.add("Insira um endereço de email valido");
            return messages;
        }

        if (!usernameRegex.matcher(username).matches()) {
            messages.add("Seu usuário deve conter de 6 a 20 caracteres e apenas os símbolos(._).");
            return messages;
        }

        return messages;
    }

    private List<String> validateRegexUpdatePassword(String newPassword) {
        List<String> messages = new ArrayList<>();

        Pattern passwordRegex = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()])(?!.*\\s).{6,30}$");

        if (!passwordRegex.matcher(newPassword).matches()) {
            messages.add("Sua senha deve conter pelo menos um número, uma letra maiúscula, um simbolo e ter de 6 a 30 caracteres");
        }

        return messages;
    }

    private boolean isEmailUnique(String email) {
        Optional<User> dbUser = repository.findByEmail(email);
        return dbUser.isEmpty();
    }

    private boolean isUsernameUnique(String username) {
        Optional<User> dbUser = repository.findByUsername(username);
        return dbUser.isEmpty();
    }

    private void validateUserData(UserUpdateDTO reqUser) throws InvalidDataException {

        List<String> messages = new ArrayList<>(validateRegexUpdate(reqUser.getEmail(), reqUser.getUsername()));

        if (!messages.isEmpty()) {
            throw new InvalidDataException(messages.get(0));
        }
    }

    @Override
    public User findById(Integer userId) throws InvalidDataException {
        if (userId == null || userId <= 0) {
            throw new InvalidDataException("O campo ID não pode ser nulo e nem igual a zero.");
        }

        return repository.findById(userId).orElseThrow(() -> new InvalidDataException("Nenhum usuário encontrado com esse id."));
    }

    @Override
    public User findByUsername(String username) throws InvalidDataException {
        if (username == null || username.isBlank()) {
            throw new InvalidDataException("O nome de usuário não pode ser nulo ou vazio.");
        }

        return repository.findByUsername(username).orElseThrow(() -> new InvalidDataException("Nenhum usuário encontrado com esse nome de usuário."));
    }

    @Override
    @Transactional
    public void updateUser(User dbUser, UserUpdateDTO reqUser) throws InvalidDataException {
        validateUserData(reqUser);

        if (!isEmailUnique(reqUser.getEmail())) {
            throw new InvalidDataException("Esse email já está cadastrado.");
        }

        if (!isUsernameUnique(reqUser.getUsername())) {
            throw new InvalidDataException("Esse nome de usuário já está cadastrado.");
        }

        User toUpdateUser = reqUser.toEntity(dbUser);

        repository.saveAndFlush(toUpdateUser);
    }

    @Override
    @Transactional
    public void updatePassword(UserPasswordDTO reqUser, User dbUser) throws InvalidDataException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(reqUser.getOldPassword(), dbUser.getPassword())) {
            throw new InvalidDataException("A senha atual não coincide com a cadastrada.");
        }

        if (passwordEncoder.matches(reqUser.getNewPassword(), dbUser.getPassword())) {
            throw new InvalidDataException("A senha nova não pode ser igual a já cadastrada no banco.");
        }

        List<String> messages = validateRegexUpdatePassword(reqUser.getNewPassword());

        if (!messages.isEmpty()) {
            throw new InvalidDataException(messages.get(0));
        }

        String encryptedNewPassword = passwordEncoder.encode(reqUser.getNewPassword());
        dbUser.setPassword(encryptedNewPassword);

        repository.saveAndFlush(dbUser);
    }

    @Override
    @Transactional
    public void removeUser(Integer id) throws InvalidDataException {
        if (id == null || id == 0) {
            throw new InvalidDataException("O campo ID não pode ser nulo e nem igual a zero.");
        }

        repository.deleteById(id);
    }


}
