package ca.onepoint.yul.classes;

import ca.onepoint.yul.dto.AvatarDto;
import ca.onepoint.yul.dto.MapDto;
import ca.onepoint.yul.dto.SquareDto;


public class MovementManagement {


    public static boolean checkIfRoute(int x, int y, SquareDto[][] map){
        return map[y][x].getValue() == 1;
    }

    public static AvatarDto move(AvatarDto avatar, MapDto map) {

        // Move the avatar to the left
        if(checkIfRoute(avatar.getX(), avatar.getY()-1, map.getSquare())) {
            avatar.setY(avatar.getY()-1);
        }

        // Move the avatar to the left
        else if(checkIfRoute(avatar.getX(), avatar.getY()+1, map.getSquare())) {
            avatar.setY(avatar.getY()+1);
        }

        // Move the avatar to upward
        else if(checkIfRoute(avatar.getX()-1, avatar.getY(), map.getSquare())) {
            avatar.setX(avatar.getX()-1);
        }

        // Move the avatar to downward
        else if(checkIfRoute(avatar.getX()+1, avatar.getY(), map.getSquare())) {
            avatar.setX(avatar.getX()+1);
        }

        return avatar;
    }
}
