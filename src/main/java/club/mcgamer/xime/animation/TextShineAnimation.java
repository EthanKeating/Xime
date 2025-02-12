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

    private final XimePlugin plugin;
    private final Profile profile;

    private final Serverable serverable;

    private int frame = 0;
    private int position = 0;
    private final String text;

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
            profile.getUser().sendMessage(Component.text(TextUtil.translate("&8&l" + text)), ChatTypes.GAME_INFO);
            return;
        }

        StringBuilder animatedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++)
            if (i == position)
                animatedText.append(ChatColor.YELLOW).append(ChatColor.BOLD).append(text.charAt(i));
            else if (i == position - 1)
                animatedText.append(ChatColor.WHITE).append(ChatColor.BOLD).append(text.charAt(i));
            else if (i < position)
                animatedText.append(ChatColor.GOLD).append(ChatColor.BOLD).append(text.charAt(i));
            else
                animatedText.append(ChatColor.DARK_GRAY).append(ChatColor.BOLD).append(text.charAt(i));

        profile.getUser().sendMessage(Component.text(TextUtil.translate(animatedText.toString())), ChatTypes.GAME_INFO);

        if (position >= text.length() - 1) cancel();
        else position++;
    }


}
