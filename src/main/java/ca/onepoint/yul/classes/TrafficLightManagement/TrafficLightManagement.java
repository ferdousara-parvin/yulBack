package ca.onepoint.yul.classes.TrafficLightManagement;

import ca.onepoint.yul.dto.MapDto;
import ca.onepoint.yul.dto.SquareDto;
import ca.onepoint.yul.service.IMapService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.List;

// Singleton static class to hold states

public class TrafficLightManagement {
    public static final String CROSSROADS = "croisement";
    public static TrafficLight[][][] allTrafficLights;

    public static void setUpTrafficLights(IMapService mapService) {
        try {
            List<MapDto> allMaps = mapService.getAllMap();
            MapDto montrealMap = allMaps.get(0);
            SquareDto[][] allSquares = montrealMap.getSquare();

            //Create 2D map that represents all the possible positions the map (each position has a traffic light array)
            allTrafficLights = new TrafficLight[allSquares[0].length][allSquares.length][6];

            for (int y = 0; y < allSquares.length; y++) {
                for (int x = 0; x < allSquares[y].length; x++) {
                    SquareDto square = allSquares[y][x];

                    // Find crossroads
                    if (square.getImage().contains(CROSSROADS)) {
                        TrafficLight[] allCarTrafficLights = createTrafficLightsFor(TrafficLight.TrafficLightType.CAR);
                        allTrafficLights[y][x] = allCarTrafficLights;
                        initTimersAt(x, y);
                    }
                }
            }
        } catch (JSONException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static TrafficLight[] createTrafficLightsFor(TrafficLight.TrafficLightType type) {
        TrafficLight.Direction[] allDirections = TrafficLight.Direction.values();
        TrafficLight[] carTrafficLights = new TrafficLight[allDirections.length];
        for (TrafficLight.Direction direction : allDirections) {
            carTrafficLights[direction.value] = new TrafficLight(type, direction);
        }
        return carTrafficLights;
    }

    private static void initTimersAt(int x, int y) {
        for (TrafficLight trafficLight : allTrafficLights[y][x]) {
            trafficLight.initTimer();
        }
    }

}
