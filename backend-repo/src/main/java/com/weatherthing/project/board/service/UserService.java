package com.weatherthing.project.board.service;

import com.weatherthing.project.board.dto.RegisterDto;
import com.weatherthing.project.board.entity.User;
import com.weatherthing.project.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User register(RegisterDto registerDto) {
        User user = new User();
        user.setUid(registerDto.getUid());
        user.setEmail(registerDto.getEmail());
        user.setNickname(registerDto.getNickname());
        user.setAge(registerDto.getAge());
//        user.setEcoScore(registerDto.getEcoScore());
        user.setGenderCode(registerDto.getGenderCode());
        user.setWeatherCode(registerDto.getWeatherCode());
        user.setRegionCode(registerDto.getRegionCode());
        return userRepository.save(user);
    }

}
