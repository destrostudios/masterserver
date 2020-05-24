package com.destrostudios.masterserver.controller.model;

import com.destrostudios.masterserver.database.schema.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTOMapper {

    public UserDTO map(User user) {
        List<Integer> ownedAppIds = user.getAppOwnerships().stream()
                .map(appOwnership -> appOwnership.getApp().getId())
                .collect(Collectors.toList());
        return UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .ownedAppIds(ownedAppIds)
                .build();
    }
}
