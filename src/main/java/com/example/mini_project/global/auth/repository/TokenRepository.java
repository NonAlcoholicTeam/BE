package com.example.mini_project.global.auth.repository;

import com.example.mini_project.domain.entity.User;
import com.example.mini_project.global.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    void deleteByUser(User user);

    Optional<Token> findByUser(User user);
}
