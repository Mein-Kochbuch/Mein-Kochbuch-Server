package com.github.flooooooooooorian.meinkochbuch.security.service;

import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final ChefUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findChefUserByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist!"));
    }

    public Optional<ChefUser> findChefUserByEmail(String email) {
        return userRepository.findChefUserByUsername(email);
    }
}
