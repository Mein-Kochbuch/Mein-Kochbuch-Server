package com.github.flooooooooooorian.meinkochbuch.controllers;


import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserProfileDto;
import com.github.flooooooooooorian.meinkochbuch.mapper.ChefUserMapper;
import com.github.flooooooooooorian.meinkochbuch.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("{userId}")
    public ChefUserProfileDto getUserById(@PathVariable String userId) {
        return ChefUserMapper.chefUserToChefUserProfileDto(userService.getUserById(userId));
    }
}
