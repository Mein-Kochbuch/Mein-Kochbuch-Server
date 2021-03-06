package com.github.flooooooooooorian.meinkochbuch.dtos.chefuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefUserPreviewDto {
    private String id;
    private String name;
}
