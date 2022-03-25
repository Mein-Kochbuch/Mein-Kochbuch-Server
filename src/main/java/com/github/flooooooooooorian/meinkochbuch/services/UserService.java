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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ChefUserRepository chefUserRepository;
    private final JwtUtilsService jwtUtilsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final IdUtils idUtils;
    private final TimeUtils timeUtils;

    public ChefUser getUserById(String id) {
        return chefUserRepository.findChefUserById(id).orElseThrow(() -> new UserDoesNotExistsException("User with id: " + id + " does not exists!"));
    }

    public ChefUser registerUser(String username, String name, String password) {
        return chefUserRepository.save(ChefUser.builder()
                .id(idUtils.generateId())
                .username(username)
                .password(passwordEncoder.encode(password))
                .authorities(Set.of(ChefAuthorities.USER))
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .joinedAt(timeUtils.now())
                .name(name)
                .build());
    }

    public LoginJWTDto login(UserLoginDto userLoginDto) {
        Authentication auth;
        try {
            UsernamePasswordAuthenticationToken usernamePasswordData = new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword());
            auth = authenticationManager.authenticate(usernamePasswordData);

        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not verified");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Credentials");
        }
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("name", ((ChefUser) auth.getPrincipal()).getId());
        return LoginJWTDto.builder()
                .authorities(auth.getAuthorities().stream()
                        .map(ChefAuthorities.class::cast)
                        .collect(Collectors.toSet()))
                .jwt(jwtUtilsService.createToken(claims, auth.getName()))
                .build();
    }
}
