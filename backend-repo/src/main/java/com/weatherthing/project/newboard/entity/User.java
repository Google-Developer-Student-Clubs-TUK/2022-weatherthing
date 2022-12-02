package com.weatherthing.project.newboard.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int uId;
    //@Email 어노테이션 적용이 안됨
    private String email;

    private String nickname;

    @Enumerated(EnumType.STRING) //Enum타입을 상수로 db에 저장
    private Gender gender;

    private int age;

    private int weahter;

    @CreationTimestamp //생성시간 저장
    private Timestamp createDate;

}
// uId, email, nickname, gender, age, weather