package com.github.flooooooooooorian.meinkochbuch.services.utils;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdUtils {

    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
