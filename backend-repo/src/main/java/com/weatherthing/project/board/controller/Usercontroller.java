package com.weatherthing.project.board.controller;


import com.weatherthing.project.board.dto.RegisterDto;
import com.weatherthing.project.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1")
public class UserController {
    private final UserService userService;

    // 회원정보 DB에 저장
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/auth")
    public ResponseEntity register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(userService.register(registerDto));
    }
}
