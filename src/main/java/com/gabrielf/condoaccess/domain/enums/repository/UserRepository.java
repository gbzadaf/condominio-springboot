package com.gabrielf.condoaccess.domain.enums.repository;

import com.gabrielf.condoaccess.domain.enums.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);


}
