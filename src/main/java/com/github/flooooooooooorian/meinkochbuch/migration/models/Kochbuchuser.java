package com.github.flooooooooooorian.meinkochbuch.migration.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", timezone = "UTC")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", timezone = "UTC")
    private Instant dateJoined;

    private String email;

    private String username;

    @JsonProperty("apple_id")
    private String appleId;

}
