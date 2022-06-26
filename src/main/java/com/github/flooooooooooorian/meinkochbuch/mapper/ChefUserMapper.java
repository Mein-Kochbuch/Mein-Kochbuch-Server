package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.chefuser.ChefUserPreviewDto;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChefUserMapper {

    public ChefUserPreviewDto chefUserToChefUserPreviewDto(ChefUser chefUser) {
        return ChefUserPreviewDto.builder()
                .id(chefUser.getId())
                .name(chefUser.getName())
                .build();
    }
}
