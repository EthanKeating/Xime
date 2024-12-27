package club.mcgamer.xime.sg.timer;


import club.mcgamer.xime.util.Pair;
import lombok.Getter;

@Getter
public class GameTimer {

    private int currentTime;
    private int initialTime;

    public void setTime(int newTime) {
        currentTime = newTime + 1;
        initialTime = newTime;
    }

    public void overrideTime(int newTime) {
        currentTime = newTime + 1;
    }

    public int decrement(){
        return --currentTime;
    }

    public int reset() {
        currentTime = initialTime + 1;
        return currentTime;
    }

    public String toString() {
        return String.format("%d:%02d", currentTime / 60, currentTime % 60);
    }

    public Pair<Integer, String> toSignificantUnit() {
        if (currentTime <= 60)
            return new Pair<>(currentTime, currentTime == 1 ? "second" : "seconds");

        int minutes = currentTime / 60;
        return new Pair<>(minutes, minutes == 1 ? "minute" : "minutes");
    }

    public int getMinutes() {
        return currentTime / 60;
    }

    public int getSeconds() {
        return currentTime % 60;
    }

    enum DisplayType {
        CLOCK_FACE,
        COMPACT_LABEL
    }
}

