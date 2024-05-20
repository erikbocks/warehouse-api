package com.bock.warehouseapi.services.impls;

import com.bock.warehouseapi.entities.User;
import com.bock.warehouseapi.entities.dtos.UserRegisterDTO;
import com.bock.warehouseapi.exceptions.InvalidDataException;
import com.bock.warehouseapi.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class AuthorizationServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    public AuthorizationServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public List<String> validateRegex(UserRegisterDTO user) {
        List<String> messages = new ArrayList<>();

        Pattern emailRegex = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        Pattern usernameRegex = Pattern.compile("(?=[a-zA-Z0-9._]{6,20}$)(?!.*[_.]{2})[^_.].*[^_.]");
        Pattern passwordRegex = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,29}");

        if (!emailRegex.matcher(user.getEmail().trim()).matches()) {
            messages.add("Insira um endereço de email valido.");
            return messages;
        }
        if (!usernameRegex.matcher(user.getUsername().trim()).matches()) {
            messages.add("Seu usuário deve conter de 6 a 30 caracteres e apenas os símbolos(._).");
            return messages;
        }
        if (!passwordRegex.matcher(user.getPassword().trim()).matches()) {
            messages.add("Sua senha deve conter pelo menos um número, uma letra maiúscula, um simbolo e ter de 6 a 30 caracteres");
            return messages;
        }

        return messages;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return repository.findBySubject(username.trim());
        } catch (Exception ex) {
            return null;
        }
    }

    public void registerUser(UserRegisterDTO userData) throws InvalidDataException {
        if (repository.findBySubject(userData.getEmail()) != null) {
            throw new InvalidDataException("Email já cadastrado.");
        } else if (repository.findBySubject(userData.getUsername()) != null) {
            throw new InvalidDataException("Nome de usuário já cadastrado.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userData.getPassword());
        User toSaveUser = new User(userData.getUsername().trim(), userData.getEmail().trim(), encryptedPassword);

        repository.saveAndFlush(toSaveUser);
    }
}
