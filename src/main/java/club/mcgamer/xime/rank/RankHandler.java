package club.mcgamer.xime.rank;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.rank.impl.Rank;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankHandler {

    private static final String DEFAULT_RANK_NAME = "Regular";

    private final XimePlugin plugin;
    private final List<Rank> rankList;

    public RankHandler(XimePlugin plugin) {
        this.plugin = plugin;

        //Rank priority is defined by the order added (First = Best, Last = Worst)
        rankList = Arrays.asList(
                new Rank("Owner", "&4&l", Arrays.asList("xime.admin")),
                new Rank("Regular", "&2", Arrays.asList("xime.regular"))
        );
    }

    public Rank getRank(String name) {
        return rankList.stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void setRank(Profile profile, Rank rank) {
        Player player = profile.getPlayer();

        player.getEffectivePermissions().forEach(permissionAttachmentInfo -> {
            if (permissionAttachmentInfo.getAttachment().getPlugin() == plugin)
                player.removeAttachment(permissionAttachmentInfo.getAttachment());
        });
        rank.getPermissions().forEach(permission -> {
            player.addAttachment(plugin, permission, true);
        });
    }

    public Rank getDefault() {
        return getRank(DEFAULT_RANK_NAME);
    }

}
