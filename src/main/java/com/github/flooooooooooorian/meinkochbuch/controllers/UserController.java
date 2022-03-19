package com.github.flooooooooooorian.meinkochbuch.controllers;


import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserProfileDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserRegistrationDto;
import com.github.flooooooooooorian.meinkochbuch.mapper.ChefUserMapper;
import com.github.flooooooooooorian.meinkochbuch.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("{userId}")
    public ChefUserProfileDto getUserById(@PathVariable String userId) {
        return ChefUserMapper.chefUserToChefUserProfileDto(userService.getUserById(userId));
    }

    @PostMapping()
    public ChefUserProfileDto register(@Valid @RequestBody ChefUserRegistrationDto chefUserRegistraionDto) {
        return ChefUserMapper.chefUserToChefUserProfileDto(userService.registerUser(chefUserRegistraionDto.getUsername(), chefUserRegistraionDto.getName(), chefUserRegistraionDto.getPassword()));
    }
}
