package club.mcgamer.xime.design.tag;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.bg.data.BGTemporaryData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.rank.RankHandler;
import club.mcgamer.xime.rank.impl.Rank;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTeam;
import club.mcgamer.xime.sg.data.SGTeamProvider;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.block.LeavesDecayEvent;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TagImpl {


    private final Profile profile;
    private final XimePlugin plugin = XimePlugin.getPlugin(XimePlugin.class);

    private final HashMap<String, String> nameToImplName = new HashMap<>();
    private final HashMap<String, Set<String>> teamToPlayerList = new HashMap<>();
    private final HashMap<String, HashMap<String, Integer>> objectiveValues = new HashMap<>();

    private Serverable currentServerable;

    private HashMap<String, String> playerNameToRankName = new HashMap<>();

    public TagImpl(Profile profile) {
        this.profile = profile;
        RankHandler rankHandler = plugin.getRankHandler();

        for(int priority = 0; priority < rankHandler.getRankList().size(); priority++) {
            Rank rank = rankHandler.getRankList().get(priority);
            createTeam(rank.getName(), rank.getColor(), priority);
            teamToPlayerList.put(rank.getName(), new HashSet<>());
        }
        createTeam("Spectator", "&7&o", 99);
        createTeam("Teammate", "&a", 79);
        createTeam("NoTeam", "&7", 98);
        createTeam("Enemy", "&c", 97);
        teamToPlayerList.put("Spectator", new HashSet<>());
        teamToPlayerList.put("NoTeam", new HashSet<>());
        teamToPlayerList.put("Teammate", new HashSet<>());
        teamToPlayerList.put("Enemy", new HashSet<>());

        for(int i = 1; i <= 12; i++) {
            createTeam("Team-" + i, "&c[#" + i + "] ", 80 + i);
            teamToPlayerList.put("Team-" + i, new HashSet<>());
        }
    }

    public void tick() {
        HashMap<String, String> newPlayerNameToRankName = new HashMap<>();

        ProfileHandler profileHandler = plugin.getProfileHandler();

        for(Profile loopProfile : new CopyOnWriteArrayList<>(profileHandler.getProfiles())) {
            if (loopProfile.getServerable() instanceof SGServerable) {
                SGServerable serverable = (SGServerable) loopProfile.getServerable();

                if (loopProfile.getServerable() instanceof SGMakerServerable) {
                    SGTeamProvider teamProvider = serverable.getGameSettings().getTeamProvider();

                    if (teamProvider.getTeamType() != TeamType.NO_TEAMS) {
                        SGTeam otherTeam = teamProvider.getTeamOriginal(loopProfile);
                        SGTeam viewerTeam = teamProvider.getTeamOriginal(profile);

                        if (viewerTeam == null && otherTeam != null) {
                            newPlayerNameToRankName.put(loopProfile.getName(), "Team-" + otherTeam.getTeamId());
                            continue;
                        }

                        if (otherTeam != null) {
                            if (otherTeam == viewerTeam) {
                                updateTeam("Teammate", "&a[#" + viewerTeam.getTeamId() + "] ");
                                newPlayerNameToRankName.put(loopProfile.getName(), "Teammate");
                            } else {
                                newPlayerNameToRankName.put(loopProfile.getName(), "Team-" + otherTeam.getTeamId());
                            }
                            continue;
                        }

                        if (serverable.getSpectatorList().contains(loopProfile)) {
                            newPlayerNameToRankName.put(loopProfile.getName(), "Spectator");
                            continue;
                        }

                        newPlayerNameToRankName.put(loopProfile.getName(), "NoTeam");
                        continue;
                    }
                }

                if (serverable.getSpectatorList().contains(loopProfile)) {
                    newPlayerNameToRankName.put(loopProfile.getName(), "Spectator");
                    continue;
                }
            }
            newPlayerNameToRankName.put(loopProfile.getName(), loopProfile.getRank().getName());
        }

        playerNameToRankName.forEach((player, team) -> {
            if (!newPlayerNameToRankName.containsKey(player)) {
                removePlayer(team, player);
            } else if (!newPlayerNameToRankName.get(player).equalsIgnoreCase(team)) {
                removePlayer(team, player);
                addPlayer(newPlayerNameToRankName.get(player), player);
            }
        });

        newPlayerNameToRankName.forEach((player, team) -> {
            if (!playerNameToRankName.containsKey(player)) {
                addPlayer(team, player);
            }
        });

        playerNameToRankName = newPlayerNameToRankName;

        //TODO: Eventually move this to a TagAdapter class
        if (profile.getServerable() instanceof SGServerable && !(profile.getServerable() instanceof SGMakerServerable)) {
            SGServerable serverable = (SGServerable) profile.getServerable();

            switch (serverable.getGameState()) {
                case LOBBY:
                case CLEANUP:
                case RESTARTING:
                    removeObjective("BOUNTY");
                    break;
                default:
                    serverable.getPlayerList().forEach(loopProfile -> {
                        if (loopProfile.getTemporaryData() instanceof SGTemporaryData) {
                            SGTemporaryData temporaryData = (SGTemporaryData) loopProfile.getTemporaryData();
                            createOrUpdateObjective(loopProfile.getName(), "BOUNTY", "&3Bounty", temporaryData.getBounty());
                        }
                    });
            }
        }

        if (objectiveValues.containsKey("BOUNTY") && !(profile.getServerable() instanceof SGServerable)) {
            removeObjective("BOUNTY");
        }

        //TODO: Eventually move this to a TagAdapter class
        if (profile.getServerable() instanceof BGServerable || (profile.getServerable() instanceof SGMakerServerable makerServerable && makerServerable.getGameSettings().isDisplayHealth())) {
            Serverable serverable = profile.getServerable();

            serverable.getPlayerList().forEach(loopProfile -> {
                createOrUpdateObjective(loopProfile.getName(), "HEALTH", "&c❤", (int)loopProfile.getPlayer().getHealth());
            });
        }

        if (objectiveValues.containsKey("HEALTH") && (!(profile.getServerable() instanceof BGServerable) && !(profile.getServerable() instanceof  SGMakerServerable))) {
            removeObjective("HEALTH");
        }

        if (objectiveValues.containsKey("HEALTH") && profile.getServerable() instanceof SGMakerServerable makerServerable && !makerServerable.getGameSettings().isDisplayHealth()) {
            removeObjective("HEALTH");
        }

        currentServerable = profile.getServerable();
    }

    public void createTeam(String teamName, String prefix, int priority) {

        nameToImplName.put(teamName, "!0000000000000" + (priority <= 9 ? "0" + priority : priority) + teamName);

        profile.getUser().sendPacket(new WrapperPlayServerTeams(
                nameToImplName.get(teamName),
                WrapperPlayServerTeams.TeamMode.CREATE,
                new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                        Component.text(""), //displayname - not required?
                        Component.text(TextUtil.translate(prefix)), //prefix
                        Component.text(""), //suffix
                        teamName.equals("Spectator") ?
                                WrapperPlayServerTeams.NameTagVisibility.HIDE_FOR_OWN_TEAM :
                                WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                        WrapperPlayServerTeams.CollisionRule.NEVER,
                        NamedTextColor.WHITE,
                        teamName.equals("Spectator") ?
                                WrapperPlayServerTeams.OptionData.FRIENDLY_CAN_SEE_INVISIBLE :
                                WrapperPlayServerTeams.OptionData.NONE
                ),
                Collections.emptyList()));
    }

    public void updateTeam(String teamName, String prefix) {

        profile.getUser().sendPacket(new WrapperPlayServerTeams(
                nameToImplName.get(teamName),
                WrapperPlayServerTeams.TeamMode.UPDATE,
                new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                        Component.text(""), //displayname - not required?
                        Component.text(TextUtil.translate(prefix)), //prefix
                        Component.text(""), //suffix
                        teamName.equals("Spectator") ?
                                WrapperPlayServerTeams.NameTagVisibility.HIDE_FOR_OWN_TEAM :
                                WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                        WrapperPlayServerTeams.CollisionRule.NEVER,
                        NamedTextColor.WHITE,
                        teamName.equals("Spectator") ?
                                WrapperPlayServerTeams.OptionData.FRIENDLY_CAN_SEE_INVISIBLE :
                                WrapperPlayServerTeams.OptionData.NONE
                ),
                Collections.emptyList()));
    }

    //Handled by player info packets
    public void addPlayer(String teamName, String playerName) {
        teamToPlayerList.get(teamName).add(playerName);

        profile.getUser().sendPacket(new WrapperPlayServerTeams(
                nameToImplName.get(teamName),
                WrapperPlayServerTeams.TeamMode.ADD_ENTITIES,
                new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                        Component.text(""),
                        Component.text(""),
                        Component.text(""), //suffix
                        WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                        WrapperPlayServerTeams.CollisionRule.ALWAYS,
                        NamedTextColor.WHITE,
                        WrapperPlayServerTeams.OptionData.FRIENDLY_CAN_SEE_INVISIBLE
                ),
                Collections.singletonList(playerName)));
    }

    public void removePlayer(String teamName, String playerName) {
        teamToPlayerList.get(teamName).remove(playerName);

        profile.getUser().sendPacket(new WrapperPlayServerTeams(
                nameToImplName.get(teamName),
                WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES,
                new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                        Component.text(""),
                        Component.text(""),
                        Component.text(""), //suffix
                        WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                        WrapperPlayServerTeams.CollisionRule.ALWAYS,
                        NamedTextColor.WHITE,
                        WrapperPlayServerTeams.OptionData.FRIENDLY_CAN_SEE_INVISIBLE
                ),
                Collections.singletonList(playerName)));
    }

    public void removeTeam(String teamName) {
        profile.getUser().sendPacket(new WrapperPlayServerTeams(
                nameToImplName.get(teamName),
                WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES,
                new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                        Component.text(""),
                        Component.text(""),
                        Component.text(""), //suffix
                        WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                        WrapperPlayServerTeams.CollisionRule.ALWAYS,
                        NamedTextColor.WHITE,
                        WrapperPlayServerTeams.OptionData.FRIENDLY_CAN_SEE_INVISIBLE
                ),
                Collections.emptyList()));
    }

    public void createOrUpdateObjective(String playerName, String objectiveName, String objectiveDisplay, int objectiveValue) {
        if (!objectiveValues.containsKey(objectiveName)) {
            objectiveValues.put(objectiveName, new HashMap<>());
            objectiveValues.get(objectiveName).put(playerName, objectiveValue);
            //Update
        } else {
            if (!objectiveValues.get(objectiveName).containsKey(playerName)) {
                objectiveValues.get(objectiveName).put(playerName, objectiveValue);
                //Update
            } else {
                if (objectiveValues.get(objectiveName).get(playerName) == objectiveValue) {
                    return;
                } else {
                    objectiveValues.get(objectiveName).put(playerName, objectiveValue);
                }
            }
        }

        profile.getUser().sendPacket(new WrapperPlayServerScoreboardObjective(
                objectiveName,
                WrapperPlayServerScoreboardObjective.ObjectiveMode.CREATE, Component.text(TextUtil.translate(objectiveDisplay)), //Title
                WrapperPlayServerScoreboardObjective.RenderType.HEARTS));

        profile.getUser().sendPacket(new WrapperPlayServerDisplayScoreboard(2, objectiveName));

        profile.getUser().sendPacket(new WrapperPlayServerUpdateScore(playerName,
                WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM,
                objectiveName,
                Optional.of(objectiveValue)
        ));
    }

    public void removeObjective(String objectiveName) {
        objectiveValues.remove(objectiveName);

        profile.getUser().sendPacket(new WrapperPlayServerScoreboardObjective(
                objectiveName,
                WrapperPlayServerScoreboardObjective.ObjectiveMode.REMOVE, Component.text(""), //Title
                WrapperPlayServerScoreboardObjective.RenderType.HEARTS));
    }

}
