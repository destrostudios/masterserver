package com.destrostudios.masterserver.model;

import com.destrostudios.masterserver.database.schema.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDtoMapper {

    public UserBasicDto mapBasic(User user) {
        return UserBasicDto.builder()
            .id(user.getId())
            .login(user.getLogin())
            .build();
    }

    public UserDetailedDto mapDetailed(User user) {
        List<Integer> ownedAppIds = user.getAppOwnerships().stream()
            .map(appOwnership -> appOwnership.getApp().getId())
            .collect(Collectors.toList());
        return UserDetailedDto.builder()
            .id(user.getId())
            .login(user.getLogin())
            .ownedAppIds(ownedAppIds)
            .build();
    }
}
