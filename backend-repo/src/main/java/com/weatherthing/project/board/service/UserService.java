package com.weatherthing.project.board.service;

import com.weatherthing.project.board.dto.SignupRequestDto;
import com.weatherthing.project.board.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this. userRepository = userRepository;
    }


    public String signup(SignupRequestDto signupRequestDto) {

        userRepository.save(signupRequestDto.toEntity());
        return "true";
    }

}
