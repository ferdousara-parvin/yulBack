package ca.onepoint.yul.classes;

public enum SquareType {
    ROOF(0),
    ROUTE(1),
    METRO(2),
    STORE(3),
    ARRIVAL(4),
    PROTESTOR(5);

    public int value;

    SquareType(int value) {
        this.value = value;
    }
}

