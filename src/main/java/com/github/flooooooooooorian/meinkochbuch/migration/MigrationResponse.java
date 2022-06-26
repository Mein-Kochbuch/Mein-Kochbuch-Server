package com.github.flooooooooooorian.meinkochbuch.migration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MigrationResponse {
    private int total;
    private int successful;
}
