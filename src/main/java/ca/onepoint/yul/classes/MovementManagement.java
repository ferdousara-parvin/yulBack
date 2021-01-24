package ca.onepoint.yul.classes;

import ca.onepoint.yul.dto.AvatarDto;
import ca.onepoint.yul.dto.MapDto;
import ca.onepoint.yul.dto.SquareDto;

import java.util.ArrayList;
import java.util.List;


public class MovementManagement {

    public static List<SquareNode> pathToDestination = new ArrayList<>();
    public static int moveCounter = 0;

    public static boolean checkIfStreet(int x, int y, SquareDto[][] map){
        return map[y][x].getValue() == 1;
    }

    public static AvatarDto move(AvatarDto avatar, MapDto map) {

        // Move the avatar to the left
        if(checkIfStreet(avatar.getX(), avatar.getY()-1, map.getSquare())) {
            avatar.setY(avatar.getY()-1);
        }

        // Move the avatar to the left
        else if(checkIfStreet(avatar.getX(), avatar.getY()+1, map.getSquare())) {
            avatar.setY(avatar.getY()+1);
        }

        // Move the avatar to upward
        else if(checkIfStreet(avatar.getX()-1, avatar.getY(), map.getSquare())) {
            avatar.setX(avatar.getX()-1);
        }

        // Move the avatar to downward
        else if(checkIfStreet(avatar.getX()+1, avatar.getY(), map.getSquare())) {
            avatar.setX(avatar.getX()+1);
        }

        return avatar;
    }

    public static List<SquareNode> getPathToTarget(SquareDto[][] map, int startX, int startY, int endX, int endY) {
        PathFinder pathFinder = new PathFinder(map, startX, startY, endX, endY);
        return  pathFinder.getPathToDestination();
    }


    public static void getPathToOnePoint(SquareDto[][] map, AvatarDto myAvatar) {
        List<SquareNode> pathToMetroEntrance = getPathToTarget(map, myAvatar.getX(), myAvatar.getY(), 9,9);
        List<SquareNode> pathToMetroExit = getPathToTarget(map, 9, 9, 15,25);
        List<SquareNode> pathToStarbucks = getPathToTarget(map,  15,25, 3, 21);
        List<SquareNode> pathToBurgerKing = getPathToTarget(map,   3, 21, 16, 14);
        List<SquareNode> pathToOnePoint = getPathToTarget(map,  16,14,13, 18);

        pathToDestination.addAll(pathToMetroEntrance);
        pathToDestination.addAll(pathToMetroExit);
        pathToDestination.addAll(pathToStarbucks);
        pathToDestination.addAll(pathToBurgerKing);
        pathToDestination.addAll(pathToOnePoint);
    }
}
