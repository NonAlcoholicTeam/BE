package com.example.mini_project.domain.repository;

import com.example.mini_project.domain.entity.User;
import com.example.mini_project.global.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    void deleteByUser(User user);
}
