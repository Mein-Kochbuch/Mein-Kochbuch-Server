package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreview;
import com.github.flooooooooooorian.meinkochbuch.mapper.CookbookMapper;
import com.github.flooooooooooorian.meinkochbuch.services.CookbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

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
}
