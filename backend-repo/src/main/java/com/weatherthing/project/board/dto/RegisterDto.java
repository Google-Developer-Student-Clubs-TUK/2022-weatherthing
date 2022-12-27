package com.weatherthing.project.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    private Long uid;
    private String email;
    private String nickname;
    private Integer age;
//    private Integer ecoScore;
    private Integer genderCode;
    private Integer weatherCode;
    private Integer regionCode;
}
