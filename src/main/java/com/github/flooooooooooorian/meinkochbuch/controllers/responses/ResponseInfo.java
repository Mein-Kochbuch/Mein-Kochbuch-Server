package com.github.flooooooooooorian.meinkochbuch.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseInfo {
    private int count;
    private int pages;
    private String next;
    private String prev;
}
