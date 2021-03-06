package com.github.flooooooooooorian.meinkochbuch.security.config;

import com.github.flooooooooooorian.meinkochbuch.security.filter.JwtAuthFilter;
import com.github.flooooooooooorian.meinkochbuch.security.service.ChefUserDetailsService;
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

    private final ChefUserDetailsService userService;
    private final JwtAuthFilter jwtAuthFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()

                .mvcMatchers(HttpMethod.GET, "/api/recipes/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/cookbooks/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/user/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/user").permitAll()

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
