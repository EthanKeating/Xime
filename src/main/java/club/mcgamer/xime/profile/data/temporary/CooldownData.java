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

    private long announceCooldown = 0;
    private long reportCooldown = 0;
    private long inviteCooldown = 0;
    private long bountyCooldown = 0;
    private long messageCooldown = 0;

    //private long chatCooldown = System.currentTimeMillis();
    public boolean hasAnnounceCooldown(double cooldownLength) {
        return checkIfOnCooldown(announceCooldown, cooldownLength) != null;
    }

    public boolean hasReportCooldown(double cooldownLength) {
        return checkIfOnCooldown(reportCooldown, cooldownLength) != null;
    }

    public boolean hasInviteCooldown(double cooldownLength) {
        return checkIfOnCooldown(inviteCooldown, cooldownLength) != null;
    }

    public boolean hasMessageCooldown(double cooldownLength) {
        return checkIfOnCooldown(messageCooldown, cooldownLength) != null;
    }

    public void setAnnounceCooldown() {
        announceCooldown = System.currentTimeMillis();
    }

    public void setReportCooldown() {
        reportCooldown = System.currentTimeMillis();
    }

    public void setInviteCooldown() {
        inviteCooldown = System.currentTimeMillis();
    }

    public void setBountyCooldown() {
        bountyCooldown = System.currentTimeMillis();
    }

    public void setMessageCooldown() {
        inviteCooldown = System.currentTimeMillis();
    }

    private String checkIfOnCooldown(long cooldownTimestamp, double cooldownLength) {

        double elapsedSeconds = (System.currentTimeMillis() - cooldownTimestamp) / 1000.0d;
        double remainingSeconds = cooldownLength - elapsedSeconds;

        if (remainingSeconds > 0) { // Still on cooldown
            int minutes = (int) (remainingSeconds / 60);
            double seconds = remainingSeconds % 60;

            String timeText = (minutes > 0 ? minutes + "m" : "") + String.format("%.1fs", seconds);

            profile.sendMessage(profile.getServerable().getPrefix() + String.format("&cYou cannot do this for another %s!", timeText));
            return timeText;
        }
        return null; // Cooldown expired
    }

}
