package club.mcgamer.xime.design.tag;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.rank.RankHandler;
import club.mcgamer.xime.rank.impl.Rank;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.util.Pair;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;

import java.util.*;

public class TagImpl {


    private final Profile profile;
    private final XimePlugin plugin = XimePlugin.getPlugin(XimePlugin.class);
    //TODO: nameTagImpl
    //
    // Create teams when player joins (Owner, Admin, SrMod, Mod, Quantum, Platinum, Diamond, Gold, Iron, Default, Spectator
    // Have a add(otherPlayer) and remove(otherPlayer) method that gets called whenever a player becomes visible/hidden from a player
    private final HashMap<String, String> nameToImplName = new HashMap<>();
    private final HashMap<String, Set<String>> teamToPlayerList = new HashMap<>();
    private final HashMap<String, HashMap<String, Integer>> objectiveValues = new HashMap<>();

    private Serverable currentServerable;

    private HashMap<String, String> playerNameToTeamName = new HashMap<>();

    public TagImpl(Profile profile) {
        this.profile = profile;
        RankHandler rankHandler = plugin.getRankHandler();

        for(int priority = 0; priority < rankHandler.getRankList().size(); priority++) {
            Rank rank = rankHandler.getRankList().get(priority);
            createTeam(rank.getName(), rank.getColor(), priority);
            teamToPlayerList.put(rank.getName(), new HashSet<>());
        }
        //createTeam("Spectator", TextUtil.translate("&7&o"), rankHandler.getRankList().size());
    }

    public void tick() {
        HashMap<String, String> newPlayerNameToRankName = new HashMap<>();

        ProfileHandler profileHandler = plugin.getProfileHandler();

        profileHandler.getProfiles().forEach(profile -> {
            newPlayerNameToRankName.put(profile.getName(), profile.getRank().getName());
        });
////
////        playerNameToTeamName.forEach((player, team) -> {
////            addPlayer(team, player);
////        });

        playerNameToTeamName.forEach((currentPlayerName, oldTeamName) -> {
            if (!newPlayerNameToRankName.containsKey(currentPlayerName)) {
                removePlayer(oldTeamName, currentPlayerName);
            }
            else if (!oldTeamName.equalsIgnoreCase(newPlayerNameToRankName.get(currentPlayerName))) {
                removePlayer(oldTeamName, currentPlayerName);
                addPlayer(newPlayerNameToRankName.get(currentPlayerName), currentPlayerName);
            }
        });

//        newPlayerNameToRankName.forEach((currentPlayerName, oldTeamName) -> {
//            if (newPlayerNameToRankName.containsKey(currentPlayerName) && !playerNameToTeamName.containsKey(currentPlayerName)) {
//                addPlayer(newPlayerNameToRankName.get(currentPlayerName), currentPlayerName);
//            }
//        });

        //TODO: Revert if causing client sided lag
        newPlayerNameToRankName.forEach((currentPlayerName, oldTeamName) -> {
            if (newPlayerNameToRankName.containsKey(currentPlayerName)) {
                addPlayer(newPlayerNameToRankName.get(currentPlayerName), currentPlayerName);
            }
        });

        playerNameToTeamName = newPlayerNameToRankName;

        //TODO: Eventually move this to a TagAdapter class
        if (profile.getServerable() instanceof SGServerable) {
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

        if (!(profile.getServerable() instanceof SGServerable)) {
            removeObjective("BOUNTY");
        }

        currentServerable = profile.getServerable();
    }

    public void createTeam(String teamName, String prefix, int priority) {

        nameToImplName.put(teamName, priority + teamName);

        profile.getUser().sendPacket(new WrapperPlayServerTeams(
                nameToImplName.get(teamName),
                WrapperPlayServerTeams.TeamMode.CREATE,
                new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                        Component.text(""), //displayname - not required?
                        Component.text(TextUtil.translate(prefix)), //prefix
                        Component.text(""), //suffix
                        WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                        WrapperPlayServerTeams.CollisionRule.ALWAYS,
                        NamedTextColor.WHITE,
                        WrapperPlayServerTeams.OptionData.FRIENDLY_CAN_SEE_INVISIBLE
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
                teamToPlayerList.get(teamName)));
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
                teamToPlayerList.get(teamName)));
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
