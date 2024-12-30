package club.mcgamer.xime.rank;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.rank.impl.Rank;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RankHandler {

    private final XimePlugin plugin;
    @Getter private final List<Rank> rankList;
    public static final Rank DEFAULT_RANK = new Rank("Regular", "&2", Collections.emptyList());

    public RankHandler(XimePlugin plugin) {
        this.plugin = plugin;

        //Rank priority is defined by the order added (First = Best, Last = Worst)
        rankList = Arrays.asList(
                new Rank("Owner", "&4&l", Arrays.asList("xime.admin")),
                new Rank("Administrator", "&4&l", Arrays.asList("xime.admin")),
                new Rank("Devmin", "&9&l", Arrays.asList("xime.admin")),
                new Rank("Developer", "&e&l", Arrays.asList("xime.admin")),
                new Rank("SeniorModerator", "&4", Arrays.asList("xime.regular")),
                new Rank("Moderator", "&4", Arrays.asList("xime.regular")),
                new Rank("VIP", "&5", Arrays.asList("xime.regular")),
                new Rank("MapMaker", "&d", Arrays.asList("xime.regular")),
                new Rank("Platinum", "&b", Arrays.asList("xime.regular")),
                new Rank("Diamond", "&3", Arrays.asList("xime.regular")),
                new Rank("Gold", "&6", Arrays.asList("xime.regular")),
                new Rank("Iron", "&7", Arrays.asList("xime.regular")),
                DEFAULT_RANK
        );
    }

    public Rank getRank(String name) {
        return rankList.stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void setRank(Profile profile, Rank rank) {
        Player player = profile.getPlayer();

        player.getEffectivePermissions().forEach(permissionAttachmentInfo -> {
            if (permissionAttachmentInfo != null && permissionAttachmentInfo.getAttachment() != null && permissionAttachmentInfo.getAttachment().getPlugin() == plugin)
                player.removeAttachment(permissionAttachmentInfo.getAttachment());
        });
        rank.getPermissions().forEach(permission -> {
            player.addAttachment(plugin, permission, true);
        });

        profile.setRank(rank);
    }

}
