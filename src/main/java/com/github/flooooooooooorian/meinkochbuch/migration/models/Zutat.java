package com.github.flooooooooooorian.meinkochbuch.migration.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Zutat {

    private int id;
    private String zutat;
    private float menge;
    private GlobalZutat globalZutat;
    private int rezept_id;
}
