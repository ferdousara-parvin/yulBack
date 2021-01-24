package ca.onepoint.yul.service;

import ca.onepoint.yul.dto.AvatarDto;

import java.util.List;

public interface IAvatarService {

    AvatarDto getAvatarById(Integer id);

    List<AvatarDto> getAllAvatars();

    List<AvatarDto> getAvatarsByType(Integer type);

    List<AvatarDto> getAllAvatarsButFireworks();
}
