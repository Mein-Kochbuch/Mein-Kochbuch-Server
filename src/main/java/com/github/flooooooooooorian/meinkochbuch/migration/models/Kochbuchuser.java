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
public class Kochbuchuser {

    private int id;
    private String password;

    @JsonProperty("last_login")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private Instant lastLogin;

    @JsonProperty("is_superuser")
    private boolean isSuperUser;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("is_active")
    private boolean isActive;

    @JsonProperty("date_joined")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private Instant dateJoined;

    private String email;

    private String username;

    @JsonProperty("apple_id")
    private String appleId;

}
