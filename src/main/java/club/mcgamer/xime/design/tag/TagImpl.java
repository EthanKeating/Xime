package club.mcgamer.xime.design.tag;

import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.util.ColorUtil;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TagImpl {


    //TODO: nameTagImpl
    //
    // Create teams when player joins (Owner, Admin, SrMod, Mod, Quantum, Platinum, Diamond, Gold, Iron, Default, Spectator
    // Have a add(otherPlayer) and remove(otherPlayer) method that gets called whenever a player becomes visible/hidden from a player
    private HashMap<String, String> nameToImplName = new HashMap<>();

    private Set<String> currentPlayerNames = new HashSet<>();

    public void createTeam(String teamName, int priority, String prefix) {

        nameToImplName.put(teamName, priority + teamName);

        new WrapperPlayServerTeams(
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
                Collections.emptyList()
        );
    }

    public void addPlayer(String teamName, String playerName) {
        new WrapperPlayServerTeams(
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
                Collections.singletonList(playerName));
    }

    public void removePlayer(String teamName, String playerName) {
        new WrapperPlayServerTeams(
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
                Collections.singletonList(playerName));
    }

    public void removeTeam(String teamName) {
        new WrapperPlayServerTeams(
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
                Collections.emptyList());
    }

}
