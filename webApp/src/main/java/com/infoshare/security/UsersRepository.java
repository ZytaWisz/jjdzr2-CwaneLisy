package com.infoshare.security;

import com.infoshare.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUserName(String userName);

    Optional<User> findByUserEmail(String email);
}
