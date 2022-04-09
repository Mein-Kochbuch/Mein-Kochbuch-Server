package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookCreationDto;
import com.github.flooooooooooorian.meinkochbuch.exceptions.CookPrivacyForbiddenException;
import com.github.flooooooooooorian.meinkochbuch.exceptions.CookbookDoesNotExist;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.CookbookContent;
import com.github.flooooooooooorian.meinkochbuch.models.recipe.Recipe;
import com.github.flooooooooooorian.meinkochbuch.repository.CookbookRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CookbookService {

    private final CookbookRepository cookbookRepository;
    private final RecipeService recipeService;
    private final IdUtils idUtils;

    public List<Cookbook> findAllCookbooks(String id) {
        return cookbookRepository.findAllByPrivacyIsFalseOrOwner_Id(id);
    }

    public Cookbook findCookbookById(String cookbookId, Optional<String> optionalUserId) {
        Cookbook cookbook = cookbookRepository.findById(cookbookId).orElseThrow(() -> new CookbookDoesNotExist("Cookbook with id: " + cookbookId + " not found!"));

        if ((cookbook.isPrivacy() && optionalUserId.isEmpty()) || (cookbook.isPrivacy() && optionalUserId.isPresent() && !cookbook.getOwner().getId().equals(optionalUserId.get()))) {
            throw new CookPrivacyForbiddenException("User: " + (optionalUserId.orElseGet(() -> "Anonymous" + " is not allowed to access Cookbook: " + cookbookId)));
        }
        return cookbook;
    }

    @Transactional
    public Cookbook addCookbook(CookbookCreationDto cookbookCreationDto, String userId) {

        boolean privacy = cookbookCreationDto.isPrivacy();

        if (!privacy) {
            privacy = recipeService.getAllRecipesByIds(cookbookCreationDto.getRecipeIds()).stream()
                    .anyMatch(Recipe::isPrivacy);
        }

        String generateId = idUtils.generateId();
        Cookbook newCookbook = Cookbook.builder()
                .name(cookbookCreationDto.getName())
                .privacy(privacy)
                .owner(ChefUser.ofId(userId))
                .id(generateId)
                .contents(cookbookCreationDto.getRecipeIds().stream()
                        .map(id -> CookbookContent.builder()
                                .cookbook(Cookbook.ofId(generateId))
                                .recipe(Recipe.ofId(id))
                                .build())
                        .toList())
                //TODO add CookbookThumbnail
                .thumbnail(null)
                .build();

        return cookbookRepository.save(newCookbook);
    }
}
