package com.bock.warehouseapi.services.impls;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.UpdateUserDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.repositories.UserRepository;
import com.bock.warehouseapi.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<User> findById(Integer id) throws InvalidDataException {
        if (id == null || id == 0) {
            throw new InvalidDataException("O campo ID não pode ser nulo e nem igual a zero.");
        }
        return repository.findById(id);
    }

    @Override
    public void updateUser(User dbUser, UpdateUserDTO reqUser) throws InvalidDataException {

        List<String> messages = validateRegexUpdate(reqUser.getEmail(), reqUser.getUsername());

        if (!messages.isEmpty()) {
            throw new InvalidDataException(messages.get(0));
        }

        User toUpdateUser = reqUser.toEntity(dbUser);

        Optional<User> dbEmail = repository.findByEmail(reqUser.getEmail());
        Optional<User> dbUsername = repository.findByUsername(reqUser.getUsername());

        if (dbEmail.isPresent() && !dbEmail.get().getId().equals(reqUser.getId())) {
            throw new InvalidDataException("Esse email já está cadastrado.");
        }
        if (dbUsername.isPresent() && !dbUsername.get().getId().equals(reqUser.getId())) {
            throw new InvalidDataException("Esse nome de usuário já está cadastrado.");
        }

        repository.saveAndFlush(toUpdateUser);
    }
}
