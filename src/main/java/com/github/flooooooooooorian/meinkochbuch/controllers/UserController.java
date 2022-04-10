package com.github.flooooooooooorian.meinkochbuch.controllers;


import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserProfileDto;
import com.github.flooooooooooorian.meinkochbuch.mapper.ChefUserMapper;
import com.github.flooooooooooorian.meinkochbuch.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("{userId}")
    public ChefUserProfileDto getUserById(@PathVariable String userId, Principal principal) {
        return ChefUserMapper.chefUserToChefUserProfileDto(userService.getUserById(userId, principal != null ? Optional.of(principal.getName()) : Optional.empty()));
    }
}
