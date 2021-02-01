package ca.onepoint.yul.classes;

import ca.onepoint.yul.dto.AvatarDto;
import ca.onepoint.yul.dto.MapDto;
import ca.onepoint.yul.dto.SquareDto;
import ca.onepoint.yul.service.IMapService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ManifestationManagement {
    //Constants
    public static final int THRESHOLD = 15;
    public static final int MAP_SIZE = 30;
    public static final String PROTESTERS_IMAGE = "../assets/images/protester.png";

    // Global variables
    public static ArrayList<AvatarDto> allProtesters = new ArrayList<>();
    private static boolean protestorsBlockVerticalStreet;
    private static int randomStreetIndex;
    private static SquareDto[][] square;

    public static List<AvatarDto> getProtestors(IMapService mapService) {
        MapDto map = getFirstMap(mapService);

        if (map == null) {
            return new ArrayList<>();
        }

        square = map.getSquare();

        protestorsBlockVerticalStreet = new Random().nextBoolean(); // to determine protestors direction
        randomStreetIndex = new Random().nextInt(MAP_SIZE);
        int counter = 0;

        while (counter <= THRESHOLD) { // Threshold added so that this does not get stuck in an infinite loop
            int startIndex = getIndexForFirstRouteSquare();

            if (protestorsCanExist(startIndex)) {
                return allProtesters;
            }

            protestorsBlockVerticalStreet = new Random().nextBoolean();
            randomStreetIndex = new Random().nextInt(MAP_SIZE);
            counter++;
        }

        return allProtesters;
    }

    // Helper Methods
    private static MapDto getFirstMap(IMapService mapService) {
        MapDto map = null;
        try {
            map = mapService.getAllMap().get(0);
        } catch (JSONException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static int getIndexForFirstRouteSquare() {
        int startIndex = 0;
        SquareDto nextSquare = getNextSquare(startIndex);
        while (!nextSquare.isRoute() && startIndex < MAP_SIZE) {
            startIndex++;
            nextSquare = getNextSquare(startIndex);
        }

        return startIndex;
    }

    private static SquareDto getNextSquare(int startIndex) {
        int nextY = protestorsBlockVerticalStreet ? startIndex : randomStreetIndex;
        int nextX = protestorsBlockVerticalStreet ? randomStreetIndex : startIndex;
        return square[nextY][nextX];
    }

    //Check if there is enough space for a manifestation to happen starting from a certain point.
    // If it can, then create the protestors at the blocked street coordinates.
    private static boolean protestorsCanExist(int startIndex) {
        for (int varyingIndex = startIndex; varyingIndex < MAP_SIZE; varyingIndex++) {
            int nextY = protestorsBlockVerticalStreet ? varyingIndex : randomStreetIndex;
            int nextX = protestorsBlockVerticalStreet ? randomStreetIndex : varyingIndex;
            if (square[nextY][nextX].isRoute()) {
                int blockLength = getLengthConsecutiveBlocks(varyingIndex, protestorsBlockVerticalStreet);

                // If there is more than 1 square of route consecutively, then the manifestation can occur
                if (blockLength > 1) {
                    ArrayList<AvatarDto> addedAvatars = createProtestors(varyingIndex, blockLength);
                    allProtesters.addAll(addedAvatars);
                    return true;
                }
            }
        }
        return false;
    }

    // Returns the length of the consecutive block
    private static int getLengthConsecutiveBlocks(int startIndex, boolean isColumn) {
        for (int i = startIndex + 1; i < MAP_SIZE; i++) {
            int adjacentY = isColumn ? i : randomStreetIndex;
            int adjacentX = isColumn ? randomStreetIndex : i;
            SquareDto adjacentBlock = square[adjacentY][adjacentX];
            if (adjacentBlock.getValue() != 1)
                return i - startIndex;

        }
        return 0;
    }

    // Create manifestation from start x for a length of block length (random index indicates the row or column index)
    private static ArrayList<AvatarDto> createProtestors(int startIndex, int blockLength) {
        ArrayList<AvatarDto> listOfProtestors = new ArrayList<>();

        int minProtestors = 50;
        int totalProtestors = 0;

        while (totalProtestors < minProtestors) {
            // Create protestors for the entire length of the block with routes (street)
            for (int counter = startIndex; counter <= startIndex + blockLength - 1 && totalProtestors < minProtestors; counter++) {
                AvatarDto protestor = new AvatarDto();
                protestor.setX(protestorsBlockVerticalStreet ? randomStreetIndex : counter);
                protestor.setY(protestorsBlockVerticalStreet ? counter : randomStreetIndex);
                protestor.setImage(PROTESTERS_IMAGE);
                totalProtestors++;
                protestor.setName("Strikers" + totalProtestors);
                protestor.setType(SquareType.PROTESTOR.value);

                listOfProtestors.add(protestor);
            }
        }

        return listOfProtestors;
    }
}
