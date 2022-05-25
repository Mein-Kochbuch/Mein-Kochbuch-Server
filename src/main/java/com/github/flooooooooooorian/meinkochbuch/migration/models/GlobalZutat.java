package com.github.flooooooooooorian.meinkochbuch.migration.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlobalZutat {
    private int id;
    private String name;
    private String singular;
    private List<String> synonyms;

    private List<GlobalZutat> children;


}
