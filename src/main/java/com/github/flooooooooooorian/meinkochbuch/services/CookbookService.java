package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.exceptions.CookPrivacyForbiddenException;
import com.github.flooooooooooorian.meinkochbuch.exceptions.CookbookDoesNotExist;
import com.github.flooooooooooorian.meinkochbuch.models.cookbook.Cookbook;
import com.github.flooooooooooorian.meinkochbuch.repository.CookbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CookbookService {

    private final CookbookRepository cookbookRepository;

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
}
