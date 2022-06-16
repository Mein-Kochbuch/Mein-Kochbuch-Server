package com.github.flooooooooooorian.meinkochbuch.migration.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bild {
    private int id;
    private String image;
    private int rezept_id;
    private int owner_id;
}
