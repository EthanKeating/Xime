package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.data.SGTeam;
import club.mcgamer.xime.sg.data.SGTeamProvider;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class TeamSelectionSubMenu extends FastInv {

    public TeamSelectionSubMenu(Profile profile, SGMakerServerable serverable) {
        super(9 + (9 * (serverable.getGameSettings().getTeamProvider().getTeams().size() / 9)), TextUtil.translate("Team Selection"));

        SGTeamProvider teamProvider = serverable.getGameSettings().getTeamProvider();

        for(int i = 0; i < teamProvider.getTeams().size(); i++) {
            SGTeam team = teamProvider.getTeam(i + 1);


            setItem(i, new ItemBuilder(Material.WOOL)
                            .data(9)
                            .amount(team.getPlayers().size())
                    .name(String.format("&bTeam %s %s(%s/%s)", team.getTeamId(), team.isFull() ? "&c" : "&a", team.getPlayers().size(), team.getMaxPlayers()))
                            .lore(team.getPlayers().stream().map(loopProfile -> "&8- &2" + loopProfile.getDisplayNameBypassDisguise()).toList())
                            .build(),
                    e -> {
                        e.setCancelled(true);
                        teamProvider.addPlayer(profile, team.getTeamId());

                        serverable.getPlayerList().forEach(loopProfile -> {
                            Player loopPlayer = loopProfile.getPlayer();

                            if (loopPlayer.getOpenInventory().getTopInventory().getHolder() instanceof TeamSelectionSubMenu) {
                                new TeamSelectionSubMenu(loopProfile, serverable).open(loopPlayer);
                                loopPlayer.updateInventory();
                            }

                        });
                    });
        }
    }
}

