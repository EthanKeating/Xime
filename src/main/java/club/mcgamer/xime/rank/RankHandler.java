package club.mcgamer.xime.rank;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.rank.impl.Rank;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RankHandler {

    private final XimePlugin plugin;
    @Getter private final List<Rank> rankList;
    public static final Rank DEFAULT_RANK = new Rank("Regular", "&2", Collections.singletonList("xime.regular"));

    public RankHandler(XimePlugin plugin) {
        this.plugin = plugin;

        //Rank priority is defined by the order added (First = Best, Last = Worst)
        rankList = Arrays.asList(
                new Rank("Owner", "&4&l", Arrays.asList("xime.owner")),
                new Rank("Devmin", "&9&l", Arrays.asList()),
                new Rank("Developer", "&e&l", Arrays.asList()),
                new Rank("Administrator", "&4&l", Arrays.asList("xime.admin")),
                new Rank("SeniorModerator", "&4", Arrays.asList("xime.srmoderator")),
                new Rank("Moderator", "&c", Arrays.asList("litebans.notify", "litebans.history", "litebans.dupeip", "litebans.banlist", "litebans.mutelist", "litebans.checkban", "litebans.warnings.self", "litebans.warnlist", "litebans.ban", "litebans.kick", "litebans.mute", "litebans.warn", "litebans.unban", "litebans.staffhistory", "litebans.unwarn.own", "litebans.unmute.own", "litebans.unban.own", "litebans.unmute", "litebans.unwarn", "litebans.override", "grim.alerts", "grim.brand", "xime.staff")),
                new Rank("VIP", "&5", Arrays.asList()),
                new Rank("MapMaker", "&d", Arrays.asList("xime.vip")),
                new Rank("Quantum", "&a", Arrays.asList("xime.quantum")),
                new Rank("Platinum", "&b", Arrays.asList("xime.platinum")),
                new Rank("Diamond", "&3", Arrays.asList("xime.diamond")),
                new Rank("Gold", "&6", Arrays.asList("xime.gold")),
                new Rank("Iron", "&7", Arrays.asList("xime.iron")),
                DEFAULT_RANK
        );
    }

    public Rank getRank(String name) {
        return rankList.stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
