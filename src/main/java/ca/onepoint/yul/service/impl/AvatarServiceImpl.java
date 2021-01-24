package ca.onepoint.yul.service.impl;

import ca.onepoint.yul.dto.AvatarDto;
import ca.onepoint.yul.entity.Avatar;
import ca.onepoint.yul.repository.AvatarRepository;
import ca.onepoint.yul.service.IAvatarService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service(value = "AvatarService")
public class AvatarServiceImpl implements IAvatarService {

    @Resource
    private AvatarRepository avatarRepository;

    @Override
    public AvatarDto getAvatarById(Integer id) {
        return avatarRepository.findByIdAvatar(id);
    }

    @Override
    public List<AvatarDto> getAllAvatars() {
        List<Avatar> result = new ArrayList<>();
        avatarRepository.findAll().forEach(result::add);
        return mapAvatarToDto(result);
    }

    @Override
    public List<AvatarDto> getAllAvatarsButFireworks() {
        List<Avatar> result = new ArrayList<>();
        avatarRepository.getAllAvatorsButFireworks().forEach(result::add);
        return mapAvatarToDto(result);
    }

    @Override
    public List<AvatarDto> getAvatarsByType(Integer type) {
        return mapAvatarToDto(avatarRepository.findByType(type));
    }

    private List<AvatarDto> mapAvatarToDto(List<Avatar> avatarIterable) {
        List<AvatarDto> avatarDtoList = new ArrayList<>();
        for (Avatar avatar : avatarIterable) {
            AvatarDto avatarDto = new AvatarDto();
            avatarDto.setId(avatar.getId());
            avatarDto.setImage(avatar.getImage());
            avatarDto.setName(avatar.getName());
            avatarDto.setWaiting(avatar.getWaiting());
            avatarDto.setMain(avatar.getMain());
            avatarDto.setType(avatar.getType());
            avatarDto.setX(avatar.getX());
            avatarDto.setY(avatar.getY());
            avatarDtoList.add(avatarDto);
        }
        return avatarDtoList;
    }
}
