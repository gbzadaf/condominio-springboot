package com.gabrielf.condoaccess.service;

import com.gabrielf.condoaccess.domain.enums.entity.User;
import com.gabrielf.condoaccess.domain.enums.repository.UserRepository;
import com.gabrielf.condoaccess.dto.UserRequest;
import com.gabrielf.condoaccess.dto.UserResponse;
import com.gabrielf.condoaccess.exception.DuplicateResourceException;
import com.gabrielf.condoaccess.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse create(UserRequest request){
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Usuário com esse email %s já existe".formatted(request.email()));

        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build();

        User saved = userRepository.save(user);
        return UserResponse.from(saved);

    }

    public UserResponse findById(UUID id) {
        User user = findUserOrThrow(id);
        return UserResponse.from(user);

    }

    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserResponse::from);

    }

    @Transactional
    public UserResponse update(UUID id, UserRequest request) {
        User user = findUserOrThrow(id);

        user.setName(request.name());
        user.setEmail(request.email());
        user.setRole(request.role());

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User updated = userRepository.save(user);
        return UserResponse.from(updated);

    }

    @Transactional
    public void delete(UUID id) {
        User user = findUserOrThrow(id);
        user.softDelete();
        userRepository.save(user);

    }



    private User findUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));

    }


}
