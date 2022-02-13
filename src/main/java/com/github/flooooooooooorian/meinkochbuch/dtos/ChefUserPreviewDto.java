package com.github.flooooooooooorian.meinkochbuch.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefUserPreviewDto {
    private Long id;
    private String name;
}
