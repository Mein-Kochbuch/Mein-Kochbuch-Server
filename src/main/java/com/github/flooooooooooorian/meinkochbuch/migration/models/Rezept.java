package com.github.flooooooooooorian.meinkochbuch.migration.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.flooooooooooorian.meinkochbuch.migration.services.MultiDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rezept {
    private int id;
    private boolean privacy;
    private String name;
    private String instruction;
    private int duration;
    private int portions;
    private String thumbnail;
    @JsonProperty("owner_id")
    private int ownerId;
    private int relevanz;

    @JsonProperty("date_created")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private Instant dateCreated;

    @JsonProperty("avg_rating")
    private float avgRating;
    @JsonProperty("difficulty_id")
    private int difficultyId;
}
