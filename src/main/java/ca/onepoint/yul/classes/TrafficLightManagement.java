package ca.onepoint.yul.classes;

import ca.onepoint.yul.dto.MapDto;
import ca.onepoint.yul.dto.SquareDto;
import ca.onepoint.yul.service.IMapService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.List;

// Singleton static class to hold states

public class TrafficLightManagement {
    public static TrafficLight[][][] allTrafficLights;
    public static boolean isRushHour = false;

    public static void setUpTrafficLights(IMapService mapService) {

        try {
            List<MapDto> allMaps = mapService.getAllMap();
            MapDto montrealMap = allMaps.get(0);
            SquareDto[][] allSquares = montrealMap.getSquare();

            //Create 2D map that represents all the possible positions the map (each position has a traffic light array)
            allTrafficLights = new TrafficLight[allSquares[0].length][allSquares.length][6];


            for (int y = 0; y < allSquares.length; y++) {
                for (int x = 0; x < allSquares[y].length; x++) {
                    // Find crossroads
                    SquareDto square = allSquares[y][x];

                    if (square.getImage().contains("croisement")) {
                        // Create car traffic lights
                        TrafficLight horizontalCarTrafficLight = new TrafficLight(TrafficLight.TrafficLightType.CAR, TrafficLight.Direction.HORIZONTAL);
                        TrafficLight verticalCarTrafficLight = new TrafficLight(TrafficLight.TrafficLightType.CAR, TrafficLight.Direction.VERTICAL);
                        TrafficLight leftTurnCarTrafficLight = new TrafficLight(TrafficLight.TrafficLightType.CAR, TrafficLight.Direction.LEFT_TURN);
                        TrafficLight rightTurnCarTrafficLight = new TrafficLight(TrafficLight.TrafficLightType.CAR, TrafficLight.Direction.RIGHT_TURN);

                        //TODO: Add pedestrian traffic lights


                        // Configure lights to be opposite

                        // Initialize the timer for all of the traffic lights
                        horizontalCarTrafficLight.initTimer();
                        verticalCarTrafficLight.initTimer();
                        //TODO: Init pedestrian traffic lights

                        // Add new created traffic lights to the list of all traffic lights
                        allTrafficLights[y][x][TrafficLight.Direction.HORIZONTAL.value] = horizontalCarTrafficLight;
                        allTrafficLights[y][x][TrafficLight.Direction.VERTICAL.value] = verticalCarTrafficLight;
                        allTrafficLights[y][x][TrafficLight.Direction.LEFT_TURN.value] = leftTurnCarTrafficLight;
                        allTrafficLights[y][x][TrafficLight.Direction.RIGHT_TURN.value] = rightTurnCarTrafficLight;
                    }
                }
            }
        } catch (JSONException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
