package com.github.flooooooooooorian.meinkochbuch.models.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingKey implements Serializable {
    private String user;
    private String recipe;
}
