package com.github.flooooooooooorian.meinkochbuch.security.config;

import com.github.flooooooooooorian.meinkochbuch.security.filter.JwtAuthFilter;
import com.github.flooooooooooorian.meinkochbuch.security.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserSecurityService userService;
    private final JwtAuthFilter jwtAuthFilter;

    @Override
    @SuppressWarnings(value = "java:S5344")
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Override
    @SuppressWarnings(value = "java:S4502")
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()

                .mvcMatchers(HttpMethod.GET, "/api/recipe/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/cookbook/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/user/**").permitAll()

                .mvcMatchers(HttpMethod.POST, "/auth/**").permitAll()

                .mvcMatchers("/api/**").authenticated()
                .mvcMatchers("/**").permitAll()
                .and().addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
