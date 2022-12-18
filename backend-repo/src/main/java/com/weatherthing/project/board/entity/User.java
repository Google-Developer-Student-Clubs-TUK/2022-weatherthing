package com.weatherthing.project.board.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @Id
    private Long uId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Long age;

    @Column(nullable = false, name = "eco_score")
    private Long ecoScore;

    @Column(nullable = false, name = "gender_code")
    private Long genderCode;

//    @Column(nullable = false, name = "region_code")
//    private Long regionCode;
//
//    @Column(nullable = false, name = "weather_code")
//    private Long weatherCode;

//    @Column(nullable = false)
//    @Builder.Default
//    private boolean isDeleted = Boolean.FALSE;

//    @JsonIgnore
//    @OneToMany(mappedBy = "user")
//    private List<Board> boards = new ArrayList<>();
}
