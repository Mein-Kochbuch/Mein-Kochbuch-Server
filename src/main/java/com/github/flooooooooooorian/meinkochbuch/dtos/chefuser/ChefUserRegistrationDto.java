package com.github.flooooooooooorian.meinkochbuch.dtos.chefuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefUserRegistrationDto {
    @Email
    private String username;
    @NotEmpty
    private String name;
    private String password;
}
