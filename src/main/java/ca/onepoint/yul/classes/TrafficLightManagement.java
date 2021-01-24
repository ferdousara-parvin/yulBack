package ca.onepoint.yul.classes;

import ca.onepoint.yul.controller.MapController;
import ca.onepoint.yul.dto.MapDto;
import ca.onepoint.yul.dto.SquareDto;
import ca.onepoint.yul.service.IMapService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.List;

// Singleton static class to hold states
public class TrafficLightManagement {
    public static TrafficLight[][][] allTrafficLights;

    public static void setUpTrafficLights(IMapService mapService) {

        try {
            List<MapDto> allMaps = mapService.getAllMap();
            MapDto montrealMap = allMaps.get(0);
            SquareDto[][] allSquares = montrealMap.getSquare();

            //Create 2D map that represents all the possible positions the map (each position has a traffic light array)
            allTrafficLights = new TrafficLight[allSquares[0].length][allSquares.length][4];


            for (int y = 0; y < allSquares.length; y++) {
                for (int x = 0; x < allSquares[y].length; x++) {
                    // Find crossroads
                    SquareDto square = allSquares[y][x];

                    if (square.getImage().contains("croisement")) {
                        // Create car traffic lights
                        TrafficLight horizontalCarTrafficLight = new TrafficLight(TrafficLight.TrafficLightType.CAR, TrafficLight.Direction.HORIZONTAL);
                        TrafficLight verticalCarTrafficLight = new TrafficLight(TrafficLight.TrafficLightType.CAR, TrafficLight.Direction.VERTICAL);

                        //TODO: Add pedestrian traffic lights

                        // Initialize the timer for all of the traffic lights
                        horizontalCarTrafficLight.initTimer();
                        verticalCarTrafficLight.initTimer();
                        //TODO: Init pedestrian traffic lights

                        // Add new created traffic lights to the list of all traffic lights
                        allTrafficLights[y][x][0] = horizontalCarTrafficLight;
                        allTrafficLights[y][x][1] = verticalCarTrafficLight;
                    }
                }
            }
        } catch (JSONException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
