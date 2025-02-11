package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.data.SGTeam;
import club.mcgamer.xime.sg.data.SGTeamProvider;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TeamSettingsMenu extends FastInv {

    public static ItemStack TEAM_ITEM = new ItemBuilder(Material.LEATHER_CHESTPLATE).name("&bTeam Selector").build();

    private final SGMakerServerable serverable;

    public TeamSettingsMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(18, TextUtil.translate("Team Settings"));

        this.serverable = serverable;

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        for(int index = 0; index < TeamType.values().length; index++) {
            TeamType type = TeamType.values()[index];

            setItem(index + 2, new ItemBuilder(Material.WOOL)
                            .data(9)
                            .amount(1)
                            .name("&b" + type.getName())
                            .build(),
                    e -> {
                        serverable.getGameSettings().getTeamProvider().setTeamType(type);
                        serverable.announce("&6Team Mode: &e" + type.getName());
                        previousMenu.open(profile.getPlayer());

                        if (type == TeamType.NO_TEAMS) {
                            serverable.getPlayerList().forEach(profile1 -> {
                                profile1.getPlayer().getInventory().setItem(0, new ItemStack(Material.AIR));
                                profile1.getPlayer().closeInventory();
                            });
                        } else {
                            serverable.getPlayerList().forEach(profile1 -> profile1.getPlayer().getInventory().setItem(0, TEAM_ITEM));
                        }
                    });

            setItem(12, new ItemBuilder(Material.IRON_SWORD)
                            .name("&bFriendly Fire")
                            .build(),
                    e -> {
                        new TeamDamageSubMenu(this, profile, serverable).open(profile.getPlayer());
                    });
            setItem(14, new ItemBuilder(Material.EYE_OF_ENDER)
                            .name("&bRandomize Teams")
                            .build(),
                    e -> {
                        profile.sendMessage(serverable.getPrefix() + " &aTeams have been auto balanced and randomized.");
                        balanceTeams();
                    });
        }
    }

    public void balanceTeams() {
        SGTeamProvider teamProvider = serverable.getGameSettings().getTeamProvider();
        List<Profile> players = serverable.getPlayerList();
        teamProvider.setTeamType(teamProvider.getTeamType());

        List<SGTeam> teams = new ArrayList<>(teamProvider.getTeams());
        int numTeams = Math.min(teamProvider.getTeams().size(), (int) Math.ceil((double) players.size() / teams.size()) + 1);

        Collections.shuffle(players, new Random()); // Shuffle for randomness

        int teamIndex = 0;
        for (Profile player : players) {
            teams.get(teamIndex).addPlayer(player);
            teamIndex = (teamIndex + 1) % numTeams; // Distribute evenly
        }
    }
}

