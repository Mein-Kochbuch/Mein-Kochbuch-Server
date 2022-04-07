package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreview;
import com.github.flooooooooooorian.meinkochbuch.mapper.CookbookMapper;
import com.github.flooooooooooorian.meinkochbuch.services.CookbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cookbooks")
@RequiredArgsConstructor
public class CookbookController {

    private final CookbookService cookbookService;

    @GetMapping
    public List<CookbookPreview> getAllCookbooks(Principal principal) {
        return cookbookService.findAllCookbooks(principal != null ? principal.getName() : null).stream()
                .map(CookbookMapper::cookbookToCookbookPreview)
                .toList();
    }

    @GetMapping("{id}")
    public CookbookDto getCookbookById(@PathVariable String id, Principal principal) {
        return CookbookMapper.cookbookToCookbookDto(cookbookService.findCookbookById(id, principal != null ? Optional.ofNullable(principal.getName()) : Optional.empty()));
    }
}
