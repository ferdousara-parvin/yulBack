package ca.onepoint.yul.service.impl;

import ca.onepoint.yul.dto.MapDto;
import ca.onepoint.yul.dto.SquareDto;
import ca.onepoint.yul.entity.Map;
import ca.onepoint.yul.repository.MapRepository;
import ca.onepoint.yul.service.IMapService;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service(value = "MapService")
public class MapServiceImpl implements IMapService {

    @Resource
    private MapRepository mapRepository;

    @Override
    public MapDto getMapById(Long id) throws JSONException {
        Map map = mapRepository.findByIdMap(id);
        return mapEntiteToDto(map);
    }

    @Override
    public List<MapDto> getAllMap() throws JSONException {
        List<Map> mapList = mapRepository.findAllMap();
        List<MapDto> mapListDto = new ArrayList<>();

        for (Map map : mapList) {
            mapListDto.add(mapEntiteToDto(map));
        }
        return mapListDto;
    }

    private MapDto mapEntiteToDto(Map map) throws JSONException {
        MapDto mapDto = new MapDto();
        JSONArray jsonArr = new JSONArray(map.getMap().replace("\\", "").substring(1, map.getMap().replace("\\", "").length() - 1));
        mapDto.setId(map.getId());
        mapDto.setName(map.getName());
        SquareDto[][] squareDtoList = new SquareDto[30][30];
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONArray jsonArray = jsonArr.getJSONArray(i);
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObj = jsonArray.getJSONObject(j);
                SquareDto squareDto = new SquareDto();
                squareDto.setImage((String) jsonObj.get("image"));
                squareDto.setValue(Integer.valueOf((String) jsonObj.get("value")));
                squareDtoList[i][j] = squareDto;
            }
        }
        mapDto.setSquare(squareDtoList);
        return mapDto;
    }

    public ArrayList getRandomStreet() throws JSONException {
        MapDto map = this.getAllMap().get(0);
        SquareDto[][] square = map.getSquare();
        Random rand = new Random();
        boolean isColumn = rand.nextBoolean();
        int index = rand.nextInt(30);

        // road --> augmente block
        // come accross somethign not a road --> return the block you had thus far or

        if(isColumn) {

            for(int i = 0; i < 30; i++) {
                if(square[i][index].getValue() == 1) {
                    if(i+1 > 30 && square[i+1][index].getValue() == 1) {
                        if()
                    }

                }
            }




            int blocks = 0;

            for(int i = 0; i < 30; i++) {
                if(square[i][index].getValue() == 1)
                    blocks++;
                else if (square[i][index].getValue() != 1 && blocks >= 3)
                    break;
                else
                    blocks = 0;
            }

        }

    }

    private void getConsecutiveBlocks(int x, boolean isColumn, SquareDto[][] square) {

    }
}
