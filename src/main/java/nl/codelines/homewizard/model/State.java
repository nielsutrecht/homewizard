package nl.codelines.homewizard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class State {
    private final boolean powerOn;
    private final boolean switchLock;
    private final int brightness;

    public State(
            @JsonProperty("power_on") boolean powerOn,
            @JsonProperty("switch_lock") boolean switchLock,
            @JsonProperty("brightness") int brightness) {
        this.powerOn = powerOn;
        this.switchLock = switchLock;
        this.brightness = brightness;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public boolean isSwitchLock() {
        return switchLock;
    }

    public int getBrightness() {
        return brightness;
    }

    @Override
    public String toString() {
        return "State{" +
                "powerOn=" + powerOn +
                ", switchLock=" + switchLock +
                ", brightness=" + brightness +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return powerOn == state.powerOn && switchLock == state.switchLock && brightness == state.brightness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(powerOn, switchLock, brightness);
    }
}
