package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.UserDoesNotExistsException;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.LoginJWTDto;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.UserLoginDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import com.github.flooooooooooorian.meinkochbuch.security.service.JwtUtilsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ChefUserRepository chefUserRepository;
    private final JwtUtilsService jwtUtilsService;
    private final AuthenticationManager authenticationManager;

    public ChefUser getUserById(Long id) {
        return chefUserRepository.findChefUserById(id).orElseThrow(() -> new UserDoesNotExistsException("User with id: " + id + " does not exists!"));
    }

    public ChefUser addRecipeToUser(ChefUser user, Recipe recipe) {
        List<Recipe> recipes = user.getRecipes();
        if (recipes == null) {
            recipes = new ArrayList<>();
        }
        recipes.add(recipe);
        user.setRecipes(recipes);
        return chefUserRepository.save(user);
    }

    public LoginJWTDto login(UserLoginDto userLoginDto) {
        Authentication auth;
        try {
            UsernamePasswordAuthenticationToken usernamePasswordData = new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword());
            auth = authenticationManager.authenticate(usernamePasswordData);

        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not verified");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Credentials");
        }
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("name", ((ChefUser) auth.getPrincipal()).getName());
        return LoginJWTDto.builder()
                .authorities(auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new))
                .jwt(jwtUtilsService.createToken(claims, auth.getName()))
                .build();
    }
}
