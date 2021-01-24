package ca.onepoint.yul.classes;

import ca.onepoint.yul.dto.AvatarDto;
import ca.onepoint.yul.dto.SquareDto;

import java.util.ArrayList;
import java.util.List;


public class MovementManagement {

    public static List<SquareNode> pathToDestination = new ArrayList<>();
    public static int moveCounter;

    // Given end coordinates and start coordinates, find the path to destination
    public static List<SquareNode> getPathToTarget(SquareDto[][] map, int startX, int startY, int endX, int endY) {
        PathFinder pathFinder = new PathFinder(map, startX, startY, endX, endY);
        return  pathFinder.getPathToDestination();
    }

    // Determine what order of path needs to be taken to go from the metro to onepoint
    public static void getPathToOnePoint(SquareDto[][] map, AvatarDto myAvatar) {
        pathToDestination = new ArrayList<>();

        List<SquareNode> pathToMetroEntrance = getPathToTarget(map, 8, 9, 9,9);
        List<SquareNode> pathToStarbucks = getPathToTarget(map,  15,25, 3, 21);
        List<SquareNode> pathToBurgerKing = getPathToTarget(map,   3, 21, 16, 14);
        List<SquareNode> pathToOnePoint = getPathToTarget(map,  16,14,13, 18);

        pathToDestination.addAll(pathToMetroEntrance);
        pathToDestination.addAll(pathToStarbucks);
        pathToDestination.addAll(pathToBurgerKing);
        pathToDestination.addAll(pathToOnePoint);

        moveCounter = 0;
    }
}
