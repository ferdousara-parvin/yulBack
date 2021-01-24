package ca.onepoint.yul.controller;

import ca.onepoint.yul.dto.AvatarDto;
import ca.onepoint.yul.dto.MapDto;
import ca.onepoint.yul.service.IAvatarService;
import ca.onepoint.yul.service.IMapService;
import ca.onepoint.yul.utility.Movement;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/avatar")
public class AvatarController {

    @Resource
    private IAvatarService iAvatarService;

    @Resource
    private IMapService iMapService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    @Operation(summary = "Get an avatar by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the avatar",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AvatarDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Avatar not found",
                    content = @Content)})
    @CrossOrigin
    @GetMapping("/id/{id}")
    public AvatarDto findById(@PathVariable Integer id) {
        return iAvatarService.getAvatarById(id);
    }

    @CrossOrigin
    @GetMapping("/")
    public List<AvatarDto> findAllAvatars() {
        return iAvatarService.getAllAvatars();
    }

    @CrossOrigin
    @GetMapping("/type/{type}")
    public List<AvatarDto> findAvatarsByType(@PathVariable Integer type) {
         return iAvatarService.getAvatarsByType(type);
    }

    @CrossOrigin
    @PostMapping("/move-avatars")
    public void moveAvatars(@RequestBody List<AvatarDto> listAvatar) throws JSONException, JsonProcessingException {
        long id = 1;
        MapDto map = iMapService.getMapById(id);

        for (int i = 0; i < listAvatar.size(); i++) {
            listAvatar.set(i, Movement.move(listAvatar.get(i), map));
        }
        messagingTemplate.convertAndSend("/topic/progress", listAvatar);
    }


}
