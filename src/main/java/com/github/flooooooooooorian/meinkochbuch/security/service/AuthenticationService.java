package com.github.flooooooooooorian.meinkochbuch.security.service;

import com.github.flooooooooooorian.meinkochbuch.security.dtos.LoginJWTDto;
import com.github.flooooooooooorian.meinkochbuch.security.dtos.UserLoginDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefAuthorities;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
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

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final ChefUserRepository chefUserRepository;
    private final IdUtils idUtils;
    private final TimeUtils timeUtils;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilsService jwtUtilsService;
    private final AuthenticationManager authenticationManager;

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
        return LoginJWTDto.builder()
                .authorities(auth.getAuthorities().stream()
                        .map(ChefAuthorities.class::cast)
                        .collect(Collectors.toSet()))
                .jwt(jwtUtilsService.createToken(claims, ((ChefUser) auth.getPrincipal()).getId()))
                .username(userLoginDto.getUsername())
                .build();
    }
}
