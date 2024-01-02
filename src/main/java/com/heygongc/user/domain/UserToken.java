package com.heygongc.user.domain;

import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor // Builder 어노테이션을 위해 필요
@NoArgsConstructor(access=PROTECTED) // No default constructor for entity 오류 해결을 위해 필요
@Table(name = "user_token")
public class UserToken {

    @Id
    @Column(name = "refresh_token", nullable = false)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_seq")
    private User user;
}
