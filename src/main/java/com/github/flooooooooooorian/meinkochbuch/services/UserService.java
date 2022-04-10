package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.UserDoesNotExistsException;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.LoginJWTDto;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.UserLoginDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import com.github.flooooooooooorian.meinkochbuch.security.service.JwtUtilsService;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import com.github.flooooooooooorian.meinkochbuch.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ChefUserRepository chefUserRepository;

    public ChefUser getUserById(String id, Optional<String> optionalUserId) {
        ChefUser chefUser = chefUserRepository.findChefUserById(id).orElseThrow(() -> new UserDoesNotExistsException("User with id: " + id + " does not exists!"));

        if (optionalUserId.isEmpty() || !optionalUserId.get().equals(id)) {
            chefUser.setRecipes(chefUser.getRecipes().stream()
                    .filter(recipe -> !recipe.isPrivacy())
                    .toList());

            chefUser.setFavoriteRecipes(List.of());
            chefUser.setCookbooks(chefUser.getCookbooks().stream()
                    .filter(cookbook -> !cookbook.isPrivacy())
                    .toList());
        }

        return chefUser;
    }
}
