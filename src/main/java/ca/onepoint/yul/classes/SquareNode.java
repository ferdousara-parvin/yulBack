package ca.onepoint.yul.classes;

import ca.onepoint.yul.dto.SquareDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;

@Data
public class SquareNode {
    int id;
    SquareDto square;
    int x;
    int y;

    SquareNode parent;
    double cost;
    double heuristic;
    ArrayList<Integer> adjacentSquareID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SquareNode that = (SquareNode) o;
        return x == that.x &&
                y == that.y;
    }
}
