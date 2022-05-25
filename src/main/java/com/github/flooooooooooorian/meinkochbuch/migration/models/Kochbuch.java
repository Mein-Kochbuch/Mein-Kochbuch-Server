package com.github.flooooooooooorian.meinkochbuch.migration.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Kochbuch {

    private int id;
    private String name;
    private boolean privacy;
    private int owner_id;
    private String thumbnail;
    private float avg_rating;
}
