package ca.onepoint.yul.classes;

import ca.onepoint.yul.dto.AvatarDto;
import ca.onepoint.yul.dto.SquareDto;

import java.util.ArrayList;
import java.util.List;


public class MovementManagement {

    // X-Y coordinates of known locations
    public static final int START_X = 8;
    public static final int START_Y = 9;
    public static final int METRO_ENTRANCE_X = 9;
    public static final int METRO_ENTRANCE_Y = 9;
    public static final int METRO_EXIT_X = 15;
    public static final int METRO_EXIT_Y = 25;
    public static final int STARBUCKS_X = 3;
    public static final int STARBUCKS_Y = 21;
    public static final int BURGER_KING_X = 16;
    public static final int BURGER_KING_y = 14;
    public static final int ONEPOINT_X = 13;
    public static final int ONEPOINT_Y = 18;

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

        List<SquareNode> pathToMetroEntrance = getPathToTarget(map, START_X, START_Y, METRO_ENTRANCE_X, METRO_ENTRANCE_Y);
        List<SquareNode> pathToStarbucks = getPathToTarget(map, METRO_EXIT_X, METRO_EXIT_Y, STARBUCKS_X, STARBUCKS_Y);
        List<SquareNode> pathToBurgerKing = getPathToTarget(map,   STARBUCKS_X, STARBUCKS_Y, BURGER_KING_X, BURGER_KING_y);
        List<SquareNode> pathToOnePoint = getPathToTarget(map,  BURGER_KING_X,BURGER_KING_y, ONEPOINT_X, ONEPOINT_Y);

        pathToDestination.addAll(pathToMetroEntrance);
        pathToDestination.addAll(pathToStarbucks);
        pathToDestination.addAll(pathToBurgerKing);
        pathToDestination.addAll(pathToOnePoint);

        moveCounter = 0;
    }
}
