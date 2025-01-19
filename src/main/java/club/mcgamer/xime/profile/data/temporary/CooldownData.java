package club.mcgamer.xime.profile.data.temporary;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.TextUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class CooldownData {

    //private static final double CHAT_COOLDOWN_LENGTH = 1.0;

    private final Profile profile;

    private long announceCooldown = System.currentTimeMillis();
    //private long chatCooldown = System.currentTimeMillis();
    public boolean hasAnnounceCooldown(double cooldownLength) {
        return checkIfOnCooldown(announceCooldown, cooldownLength) != null;
    }

    public void setAnnounceCooldown() {
        announceCooldown = System.currentTimeMillis();
    }

    private String checkIfOnCooldown(long cooldownTimestamp, double cooldownLength) {

        double seconds = (System.currentTimeMillis() - cooldownTimestamp) / 1000.0d;

        if (seconds < cooldownLength) {
            String timeText = (seconds > 60 ? ((int)seconds / 60) + "m" : "") + String.format("%.1fs", seconds % 60);

            profile.sendMessage(String.format("&8[&3Xime&8] &cYou cannot do this for another %s!", timeText));
            return timeText;
        }
        return null;
    }

}
