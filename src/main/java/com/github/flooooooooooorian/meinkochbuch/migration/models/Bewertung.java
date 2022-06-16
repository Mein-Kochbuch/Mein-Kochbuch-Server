package com.github.flooooooooooorian.meinkochbuch.migration.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bewertung {
    private int id;
    private int rezept_id;
    private int user_id;
    private float rating;
}
