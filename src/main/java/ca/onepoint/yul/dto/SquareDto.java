package ca.onepoint.yul.dto;

import ca.onepoint.yul.classes.SquareType;
import lombok.Data;

@Data
public class SquareDto {
    private Integer value;
    private String image;

    public boolean isRoute(){
        return   value == SquareType.ROUTE.value;
    }
}
