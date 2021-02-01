package ca.onepoint.yul.classes.TrafficLightManagement;

import lombok.Data;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Data
public class TrafficLight {

    public static final int DELAY = 0;
    public static final int PERIOD_IN_SAME_STATE = 30 * 1000;

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
        timer.scheduleAtFixedRate(new ToggleTrafficStateTask(this), DELAY, PERIOD_IN_SAME_STATE);
    }

    void toggleState() {
        this.state = this.state == TrafficState.GREEN ?
                TrafficState.RED :
                TrafficState.GREEN;
    }

    static class ToggleTrafficStateTask extends TimerTask {
        private final TrafficLight trafficLight;

        ToggleTrafficStateTask(TrafficLight trafficLight) {
            this.trafficLight = trafficLight;
        }

        @Override
        public void run() {
            trafficLight.toggleState();
            System.out.println("Toggle Traffic Light State: " + trafficLight.getState() + new Date());
        }
    }
}


