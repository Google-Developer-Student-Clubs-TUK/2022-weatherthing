package com.weatherthing.project.board.controller;

import com.weatherthing.project.board.dto.UserInfoDto;
import com.weatherthing.project.board.service.UserService;
import com.weatherthing.project.global.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class Usercontroller {

    private final UserService userService;

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<?> signup(UserInfoDto userInfoDto) {
        return new Response<>("true", "가입 성공", userService.userinfo(userInfoDto));
    }
}
