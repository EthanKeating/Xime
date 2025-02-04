package club.mcgamer.xime.animation;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class TextShineAnimation extends BukkitRunnable {

    private XimePlugin plugin;
    private Profile profile;

    private Serverable serverable;

    private int frame = 0;
    private int position = 0;
    private String text;

    public TextShineAnimation(XimePlugin plugin, Profile profile, String text) {
        this.plugin = plugin;
        this.profile = profile;
        this.text = "    " + TextUtil.translate(text) + "    ";
        serverable = profile.getServerable();

        runTaskTimer(plugin, 1L, 1L);
    }

    public void run() {
        if (serverable != profile.getServerable()) {
            cancel();
            return;
        }
        if (frame++ < 10) {
            profile.getUser().sendMessage(Component.text(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + TextUtil.translate(text)), ChatTypes.GAME_INFO);
            return;
        }

        // Build the animated text with shine and trail effects
        StringBuilder animatedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (i == position) {
                // Current shining character
                animatedText.append(ChatColor.YELLOW).append(ChatColor.BOLD).append(text.charAt(i));
            } else if (i == position - 1) {
                // Secondary shining character (behind the primary shine)
                animatedText.append(ChatColor.WHITE).append(ChatColor.BOLD).append(text.charAt(i));
            } else if (i < position) {
                // Trail behind the shine
                animatedText.append(ChatColor.GOLD).append(ChatColor.BOLD).append(text.charAt(i));
            } else {
                // Default color for characters ahead
                animatedText.append(ChatColor.DARK_GRAY).append(ChatColor.BOLD).append(text.charAt(i));
            }
        }

        // Send the animated text to the player
        profile.getUser().sendMessage(Component.text(TextUtil.translate(animatedText.toString())), ChatTypes.GAME_INFO);

        // Check if the shine has reached the end
        if (position >= text.length() - 1) {
            cancel(); // Stop the animation when it reaches the end
            return;
        }

        // Update the position for the next frame
        position++;
    }


}
