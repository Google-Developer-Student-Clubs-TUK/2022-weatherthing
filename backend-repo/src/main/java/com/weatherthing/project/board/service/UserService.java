package com.weatherthing.project.board.service;

import com.weatherthing.project.board.dto.UserInfoDto;
import com.weatherthing.project.board.entity.User;
import com.weatherthing.project.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User userinfo(UserInfoDto userInfoDto) {
        User user = new User();
        user.setUId(userInfoDto.getUId());
        user.setEmail(userInfoDto.getEmail());
        user.setAge(userInfoDto.getAge());
        user.setEcoScore(userInfoDto.getEcoScore());
        user.setGenderCode(userInfoDto.getGenderCode());
//        user.getRegionCode(userInfoDto.getRegionCode());
//        user.getWeatherCode(userInfoDto.getWeatherCode());
        return userRepository.save(user);
    }
}
