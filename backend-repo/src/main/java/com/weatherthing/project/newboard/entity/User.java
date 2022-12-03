package com.weatherthing.project.newboard.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long uId;

    @Column(nullable = false)//@Email 어노테이션 적용이 안됨
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) //Enum타입을 상수로 db에 저장
    private Gender gender;

    @Column(nullable = false)
    private Long age;

    @Column(nullable = false)
    private Long weahter;

    @CreationTimestamp //생성시간 저장
    private Timestamp createDate;

}
// uId, email, nickname, gender, age, weather