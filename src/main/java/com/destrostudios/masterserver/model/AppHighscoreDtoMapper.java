package com.destrostudios.masterserver.model;

import com.destrostudios.masterserver.database.schema.AppHighscore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppHighscoreDtoMapper {

    @Autowired
    private AppDtoMapper appDtoMapper;
    @Autowired
    private UserDtoMapper userDtoMapper;

    public List<AppHighscoreDto> map(List<AppHighscore> appHighscores) {
        return appHighscores.stream()
            .map(this::map)
            .collect(Collectors.toList());
    }

    public AppHighscoreDto map(AppHighscore appHighscore) {
        return AppHighscoreDto.builder()
            .id(appHighscore.getId())
            .app(appDtoMapper.mapBasic(appHighscore.getApp()))
            .context(appHighscore.getContext())
            .user(userDtoMapper.mapBasic(appHighscore.getUser()))
            .score(appHighscore.getScore())
            .metadata(appHighscore.getMetadata())
            .dateTime(appHighscore.getDateTime())
            .build();
    }
}
