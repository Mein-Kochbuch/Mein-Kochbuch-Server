package com.github.flooooooooooorian.meinkochbuch.controllers.responses;

import com.github.flooooooooooorian.meinkochbuch.dtos.cookbook.CookbookPreviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CookBookListResponse {
    private ResponseInfo info;
    private List<CookbookPreviewDto> results;
}
