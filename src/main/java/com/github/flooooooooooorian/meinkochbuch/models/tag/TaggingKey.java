package com.github.flooooooooooorian.meinkochbuch.models.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaggingKey implements Serializable {
    private Long recipe;
    private Long tag;
}
