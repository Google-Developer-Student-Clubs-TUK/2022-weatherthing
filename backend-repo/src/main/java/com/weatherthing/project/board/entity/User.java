package com.weatherthing.project.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity{

    @Id
    private Long uId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Long age;

    @Column(nullable = false)
    private Long eco_score;

    @Column(nullable = false)
    private Long gender_code;

    @Column(nullable = false)
    private Long region_code;

    @Column(nullable = false)
    private Long weather_code;

    @Column(nullable = false)
    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    //일반회원 requestDto
    @Builder
    public User(Long uId, String email, Long age, Long eco_score, Long gender_code, Long region_code, Long weather_code) {
        this.uId = uId;

        this.email = email;

        this.age = age;

        this.eco_score = eco_score;

        this.gender_code = gender_code;

        this.region_code = region_code;

        this.weather_code = weather_code;

    }
}
