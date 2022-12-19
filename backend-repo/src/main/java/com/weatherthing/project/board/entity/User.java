package com.weatherthing.project.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long uid;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, name = "eco_score")
    private Integer ecoScore;

    @Column(nullable = false, name = "gender_code")
    private Integer genderCode;

    @Column(nullable = false, name = "region_code")
    private Integer regionCode;

    @Column(nullable = false, name = "weather_code")
    private Integer weatherCode;
}