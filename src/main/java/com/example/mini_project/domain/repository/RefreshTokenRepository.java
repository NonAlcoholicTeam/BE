package com.example.mini_project.domain.repository;

import com.example.mini_project.domain.entity.User;
import com.example.mini_project.global.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteByUser(User user);

    Optional<RefreshToken> findByUser(User user);
}
