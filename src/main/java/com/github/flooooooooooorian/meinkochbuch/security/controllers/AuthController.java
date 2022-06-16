package com.github.flooooooooooorian.meinkochbuch.security.controllers;

import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserProfileDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserRegistrationDto;
import com.github.flooooooooooorian.meinkochbuch.mapper.ChefUserMapper;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.LoginJWTDto;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.UserLoginDto;
import com.github.flooooooooooorian.meinkochbuch.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("login")
    public LoginJWTDto login(@RequestBody UserLoginDto userLoginDto) {
        return authenticationService.login(userLoginDto);
    }

    @PostMapping()
    public ChefUserProfileDto register(@Valid @RequestBody ChefUserRegistrationDto chefUserRegistraionDto) {
        return ChefUserMapper.chefUserToChefUserProfileDto(authenticationService.registerUser(chefUserRegistraionDto.getUsername(), chefUserRegistraionDto.getName(), chefUserRegistraionDto.getPassword()));
    }
}
