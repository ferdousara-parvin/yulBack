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


    public static List<AvatarDto> getProtestors(IMapService mapService) {

        MapDto map = null;
        try {
            map = mapService.getAllMap().get(0);
        } catch (JSONException | JsonProcessingException e) {
            e.printStackTrace();
        }
        SquareDto[][] square = map.getSquare();
        boolean isColumn = new Random().nextBoolean();
        int rowOrColumnIndex = new Random().nextInt(30);

        int threshold = 15;
        int counter = 0;

        while (counter <= threshold) { // Threshold added so that this does not get stuck in an infinite loop
            int startIndex = 0;
            while (square[isColumn ? startIndex : rowOrColumnIndex][isColumn ? rowOrColumnIndex : startIndex].getValue() != 1 && startIndex < 30) {
                startIndex++;
            }
            for (int variyingIndex = startIndex; variyingIndex < 30; variyingIndex++) {
                if (square[isColumn ? variyingIndex : rowOrColumnIndex][isColumn ? rowOrColumnIndex : variyingIndex].getValue() == 1) {
                    int blockLength = getLengthConsecutiveBlocks(variyingIndex, rowOrColumnIndex, isColumn, square);

                    // If there is more than 1 square of route consecutively
                    if (blockLength > 1) {
                        return createManifestorAvatars(variyingIndex, rowOrColumnIndex, blockLength, isColumn, square);
                    }
                }
            }
            counter++;
            isColumn = new Random().nextBoolean();
            rowOrColumnIndex = new Random().nextInt(30);
        }

        return null;
    }

    // returns the lenght of the consecutive block
    private static int getLengthConsecutiveBlocks(int startIndex, int randomIndex, boolean isColumn, SquareDto[][] square) {
        for (int i = startIndex + 1; i < 30; i++) {
            SquareDto adjacentBlock = square[isColumn ? i : randomIndex][isColumn ? randomIndex : i];
            if (adjacentBlock.getValue() != 1)
                return i - startIndex;

        }
        return 0;
    }

    // Create manifestation from start x for a length of block length (random index indicates the row or column index)
    private static ArrayList<AvatarDto> createManifestorAvatars(int startIndex, int randomIndex, int blockLength, boolean isColumn, SquareDto[][] allSquares) {
        ArrayList<AvatarDto> listOfProtestors = new ArrayList();

        int minProtestors = 50;
        int totalProtestors = 0;

        while (totalProtestors < minProtestors) {
            // Create protestors for the entire length of the block with routes (street)
            for (int counter = startIndex; counter <= startIndex + blockLength - 1 && totalProtestors < minProtestors; counter++) {
                AvatarDto protestor = new AvatarDto();
                protestor.setX(isColumn ? randomIndex : counter);
                protestor.setY(isColumn ? counter : randomIndex);
                protestor.setImage("../assets/images/protester.png");
                totalProtestors++;
                protestor.setName("Strikers" + totalProtestors);
                protestor.setType(5);

                listOfProtestors.add(protestor);

            }
        }

        return listOfProtestors;
    }
}
