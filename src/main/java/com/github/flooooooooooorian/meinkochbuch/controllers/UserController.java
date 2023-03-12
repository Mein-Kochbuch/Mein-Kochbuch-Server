package com.github.flooooooooooorian.meinkochbuch.controllers;


import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserProfileDto;
import com.github.flooooooooooorian.meinkochbuch.mapper.ChefUserProfileMapper;
import com.github.flooooooooooorian.meinkochbuch.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ChefUserProfileMapper chefUserProfileMapper;

    @GetMapping("{userId}")
    public ChefUserProfileDto getUserProfileById(@PathVariable String userId, Principal principal) {
        return chefUserProfileMapper.chefUserToChefUserProfileDto(userService.getUserById(userId, principal != null ? Optional.of(principal.getName()) : Optional.empty()));
    }
}
