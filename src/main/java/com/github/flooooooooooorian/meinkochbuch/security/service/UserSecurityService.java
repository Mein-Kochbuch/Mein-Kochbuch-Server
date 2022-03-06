package com.github.flooooooooooorian.meinkochbuch.security.service;

import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final ChefUserRepository userRepository;

    @Override
    public ChefUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findChefUserByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist!"));
    }
}
