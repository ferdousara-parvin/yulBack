package ca.onepoint.yul.classes;

import lombok.Data;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


@Data
public class TrafficLight {
    public enum TrafficLightType {CAR, PEDESTRIAN}

    public enum Direction {HORIZONTAL, VERTICAL}

    public enum TrafficState {GREEN, RED}

    private TrafficState state;
    private Timer timer; // keep track of the duration for each colour
    private TrafficLightType type;
    private Direction direction;

    public TrafficLight(TrafficLightType type, Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    public void initTimer() {
        this.timer = new Timer();
        timer.scheduleAtFixedRate(new ToggleTrafficState(this), 0, 30 * 1000);
    }
}

class ToggleTrafficState extends TimerTask {
    TrafficLight trafficLight;

    public ToggleTrafficState(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    @Override
    public void run() {
        trafficLight.setState(trafficLight.getState() == TrafficLight.TrafficState.GREEN ? TrafficLight.TrafficState.RED : TrafficLight.TrafficState.GREEN);
        System.out.println("Toggle Traffic Light State: " + trafficLight.getState() + new Date());
    }
}
