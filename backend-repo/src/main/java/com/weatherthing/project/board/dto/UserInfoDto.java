package com.weatherthing.project.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private Long uId;
    private String email;
    private Long age;
    private Long ecoScore;
    private Long genderCode;
//    private Long regionCode;
//    private Long weatherCode;
}
