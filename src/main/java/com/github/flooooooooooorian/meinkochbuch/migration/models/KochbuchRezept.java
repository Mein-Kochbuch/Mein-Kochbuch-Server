package com.github.flooooooooooorian.meinkochbuch.migration.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KochbuchRezept {
    private int id;
    private int sammlung_id;
    private int rezept_id;
}
