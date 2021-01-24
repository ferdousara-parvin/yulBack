package ca.onepoint.yul.classes.TrafficLightManagement;

import lombok.Data;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Data
public class TrafficLight {
    public enum TrafficLightType {CAR, PEDESTRIAN}

    enum Direction {
        HORIZONTAL(0),
        VERTICAL(1),
        LEFT_TURN(2),
        RIGHT_TURN(3);

        int value;
        Direction(int value) {
            this.value = value;
        }
    }

    public enum TrafficState {GREEN, RED}

    private TrafficState state;
    private Timer timer;
    private TrafficLightType type;
    private Direction direction;

    TrafficLight(TrafficLightType type, Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    void initTimer() {
        this.timer = new Timer();
        timer.scheduleAtFixedRate(new ToggleTrafficState(this), 0, 30 * 1000);
    }
}

class ToggleTrafficState extends TimerTask {
    private TrafficLight trafficLight;

    ToggleTrafficState(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    @Override
    public void run() {
        trafficLight.setState(trafficLight.getState() == TrafficLight.TrafficState.GREEN ? TrafficLight.TrafficState.RED : TrafficLight.TrafficState.GREEN);
        System.out.println("Toggle Traffic Light State: " + trafficLight.getState() + new Date());
    }
}
