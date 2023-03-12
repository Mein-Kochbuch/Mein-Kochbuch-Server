package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.UserDoesNotExistsException;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.repository.ChefUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

            chefUser.setCookbooks(chefUser.getCookbooks().stream()
                    .filter(cookbook -> !cookbook.isPrivacy())
                    .toList());
        }

        return chefUser;
    }
}
