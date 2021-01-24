package ca.onepoint.yul.controller;

import ca.onepoint.yul.classes.ManifestationManagement;
import ca.onepoint.yul.dto.AvatarDto;
import ca.onepoint.yul.dto.MapDto;
import ca.onepoint.yul.service.IAvatarService;
import ca.onepoint.yul.service.IMapService;
import ca.onepoint.yul.classes.MovementManagement;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@EnableScheduling
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
            listAvatar.set(i, MovementManagement.move(listAvatar.get(i), map));
        }

        messagingTemplate.convertAndSend("/topic/progress", listAvatar);
    }


    @CrossOrigin
    @GetMapping("/fireworks")
    public void displayFireworks() {
        List<AvatarDto> listAvatar = iAvatarService.getAvatarsByType(7);

        for(int i =0; i < listAvatar.size(); i++) {
            listAvatar.get(i).setY((int)(Math.random()*29));
            listAvatar.get(i).setX((int)(Math.random()*29));
        }
        messagingTemplate.convertAndSend("/topic/progress", listAvatar);
    }

    @CrossOrigin
    @GetMapping("/triggerManifestation")
    public void triggerManifestation() {
        List<AvatarDto> protestors = ManifestationManagement.getProtestors(iMapService);
        messagingTemplate.convertAndSend("/topic/progress", protestors);
    }

    @CrossOrigin
    @GetMapping("/clearManifestation")
    public void clearManifestation() {
        ManifestationManagement.allProtesters = new ArrayList<>();
        messagingTemplate.convertAndSend("/topic/progress", ManifestationManagement.allProtesters);
    }
}
