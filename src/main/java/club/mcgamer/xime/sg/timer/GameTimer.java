package club.mcgamer.xime.sg.timer;


import club.mcgamer.xime.util.Pair;
import lombok.Getter;

import java.text.DecimalFormat;

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

    public Pair<String, String> toSignificantUnit() {
        if (currentTime <= 60)
            return new Pair<>(currentTime + "", currentTime == 1 ? "second" : "seconds");

        DecimalFormat minuteFormat = new DecimalFormat("#.#");
        int minutes = currentTime / 60;
        int seconds = currentTime % 60;

        double total = minutes + (seconds / 60.0);
        return new Pair<>(minuteFormat.format(total), "minutes");
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

