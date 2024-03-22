package com.heygongc.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@NoArgsConstructor(access=PROTECTED) // No default constructor for entity 오류 해결을 위해 필요
@Table(name = "user_token")
public class UserToken {

    @Id
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "user_seq", nullable = false, updatable = false)
    private Long userSeq;

    protected UserToken(String refreshToken, Long userSeq) {
        this.refreshToken = refreshToken;
        this.userSeq = userSeq;
    }

    public static UserToken saveToken(String refreshToken, Long userSeq) {
        return new UserToken(refreshToken, userSeq);
    }
}
