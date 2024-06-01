package com.example.mini_project.global.auth.entity;

import com.example.mini_project.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    // 특정 엔티티가 삭제될 때 연관된 엔티티들도 같이 삭제되도록 하는 옵션
    // User 엔티티가 삭제되면, Token 엔티티도 자동으로 삭제(회원탈퇴 대비)
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "refresh_token", nullable = false, length = 1024)
    private String refreshToken;

    public RefreshToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }
}
