package com.heygongc.member.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "nickname", length = 100)
    private String nickname;

    protected Member() {}

    public Member(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public Member(String nickname) {
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
