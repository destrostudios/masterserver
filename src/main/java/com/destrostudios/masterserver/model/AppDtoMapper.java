package com.destrostudios.masterserver.model;

import com.destrostudios.masterserver.database.schema.App;
import org.springframework.stereotype.Service;

@Service
public class AppDtoMapper {

    public AppBasicDto mapBasic(App app) {
        return AppBasicDto.builder()
            .id(app.getId())
            .name(app.getName())
            .build();
    }
}
