package com.github.flooooooooooorian.meinkochbuch.controllers;

import com.github.flooooooooooorian.meinkochbuch.controllers.cookbooks.CookbookFilterParams;
import com.github.flooooooooooorian.meinkochbuch.controllers.recipes.RecipesFilterParams;
import com.github.flooooooooooorian.meinkochbuch.controllers.responses.CookBookListResponse;
import com.github.flooooooooooorian.meinkochbuch.controllers.responses.ResponseInfo;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookCreationDto;
import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookDto;
import com.github.flooooooooooorian.meinkochbuch.mapper.CookbookMapper;
import com.github.flooooooooooorian.meinkochbuch.services.CookbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/cookbooks")
@RequiredArgsConstructor
public class CookbookController {

    private final CookbookService cookbookService;
    private final CookbookMapper cookbookMapper;

    @Value("${domain.url}")
    private String domainUrl;
    @GetMapping
    public CookBookListResponse getAllCookbooks(Principal principal, CookbookFilterParams params) {
        int count = cookbookService.cookbookCount((principal != null ? Optional.ofNullable(principal.getName()) : Optional.empty()));
        return CookBookListResponse.builder()
                .info(ResponseInfo.builder()
                        .count(count)
                        .pages(count / CookbookFilterParams.PAGE_SIZE)
                        .next(params.getPage() < (count / RecipesFilterParams.PAGE_SIZE) - 1 ? domainUrl + "/api/cookbooks?page=" + (params.getPage() + 1) : null)
                        .prev(params.getPage() > 0 ? domainUrl + "/api/cookbooks?page=" + (params.getPage() - 1) : null)
                        .build())
                .results(cookbookService.findAllCookbooks(principal != null ? principal.getName() : null).stream()
                        .map(cookbookMapper::cookbookToCookbookPreview)
                        .toList())
                .build();
    }

    @GetMapping("{id}")
    public CookbookDto getCookbookById(@PathVariable String id, Principal principal) {
        return cookbookMapper.cookbookToCookbookDto(cookbookService.findCookbookById(id, principal != null ? Optional.ofNullable(principal.getName()) : Optional.empty()));
    }

    @PostMapping
    public CookbookDto createCookbook(@Valid @RequestBody CookbookCreationDto cookbookCreationDto, Principal principal) {
        return cookbookMapper.cookbookToCookbookDto(cookbookService.addCookbook(cookbookCreationDto, principal.getName()));
    }

    @PutMapping("{cookbookId}")
    public CookbookDto editCookbook(@PathVariable @NotEmpty String cookbookId, @RequestBody CookbookCreationDto cookbookCreationDto, Principal principal) {
        return cookbookMapper.cookbookToCookbookDto(cookbookService.editCookbookById(cookbookId, cookbookCreationDto, principal.getName()));
    }
}
