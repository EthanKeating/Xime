package club.mcgamer.xime.command;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.command.map.ItemsCommand;
import club.mcgamer.xime.command.map.MapCommand;
import club.mcgamer.xime.command.server.*;
import club.mcgamer.xime.command.sg.*;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTabComplete;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommandHandler {

    private final XimePlugin plugin;

    private HashMap<Class<? extends Serverable>, HashSet<String>> blockedCommands = new HashMap<>();
    private HashSet<String> serverBlockedCommands;
    private HashSet<String> hubBlockedCommands;

    public CommandHandler(XimePlugin plugin) {
        this.plugin = plugin;

        register();
        populate();
    }

    public boolean onCommand(Profile profile, String command) {
        String unknownCommand = "Unknown command. Type \"/help\" for help.";

        Serverable serverable = profile.getServerable();
        Class<? extends Serverable> serverableClass = serverable.getClass();
        HashSet<String> globalCommands = blockedCommands.get(Serverable.class);
        HashSet<String> serverCommands = blockedCommands.get(serverableClass);

        if ((serverCommands != null && serverCommands.contains(command.toLowerCase()))
                || (globalCommands != null && globalCommands.contains(command.toLowerCase()))) {
            profile.sendMessage(unknownCommand);

            return true;
        }
        return false;
    }

    public void onTabComplete(Profile profile, List<WrapperPlayServerTabComplete.CommandMatch> commands) {
        for(WrapperPlayServerTabComplete.CommandMatch match : new CopyOnWriteArrayList<>(commands)) {
            if (serverBlockedCommands.contains(match.getText().toLowerCase())) {
                commands.remove(match);
            }

            if (profile.getServerable() instanceof HubServerable) {
                if (hubBlockedCommands.contains(match.getText().toLowerCase())) {
                    commands.remove(match);
                }
            }
        }
    }

    private void register() {
        new StaffCommand();
        new GoToCommand();

        new BringCommand();
        new ReportCommand();
        new ReportsCommand();
        new MessageCommand();
        new ReplyCommand();
        new PingCommand();
        new SilentJoinCommand();
        new MaskCommand();
        new UnmaskCommand();
        new DisguiseCommand();
        new UndisguiseCommand();
        new HelpopCommand();
        new ScrambleCommand();
        new UnscrambleCommand();
        new FlyCommand();
        new WhitelistCommand();
        new MapCommand();
        new ItemsCommand();
        new JoinCommand();
        new HubCommand();
        new RankCommand();
        new SidebarCommand();
        new XimeInfoCommand();
        new InfoCommand();
        new SetPointsCommand();
        new FixCommand();
        new ChatColorCommand();
        new GlobalListCommand();
        new ListCommand();
        new MakerCommand();
        new SecretCommand();
        new StaffChatCommand();
        new StaffListCommand();
        new InviteCommand();
        new FindCommand();
        new RealNameCommand();

        new TeamChatCommand();
        new ForcestartCommand();
        new LeaderboardCommand();
        new StatsCommand();
        new RecordsCommand();
        new VoteCommand();
        new SpectateCommand();
        new TimeLeftCommand();
        new KillCommand();
        new BountyCommand();
        new SponsorCommand();
        new AnnounceCommand();
    }

    private void populate() {

        blockedCommands.put(Serverable.class, new HashSet<>(Arrays.asList(
                "/minecraft:achievement", "/achievement",
                "/minecraft:clone", "/clone",
                "/minecraft:blockdata", "/blockdata",
                "/minecraft:debug",
                "/minecraft:defaultgamemode", "/defaultgamemode",
                "/minecraft:enchant", "/enchant",
                "/minecraft:entitydata", "/entitydata",
                "/minecraft:execute", "/execute",
                "/minecraft:fill", "/fill",
                "/minecraft:gamerule", "/gamerule",
                "/minecraft:help",
                "/minecraft:me", "/me",
                "/minecraft:list",
                "/minecraft:kill",
                "/minecraft:particle", "/particle",
                "/minecraft:replaceitem", "/replaceitem",
                "/minecraft:say", "/say",
                "/minecraft:scoreboard", "/scoreboard",
                "/minecraft:seed", "/seed",
                "/minecraft:setblock", "/setblock",
                "/minecraft:setidletimeout", "/setidletimeout",
                "/minecraft:setworldspawn", "/setworldspawn",
                "/minecraft:spawnpoint", "/spawnpoint",
                "/minecraft:spreadplayers", "/spreadplayers",
                "/minecraft:stats",
                "/minecraft:summon", "/summon",
                "/minecraft:tell", "/tell",
                "/minecraft:tellraw", "/tellraw",
                "/minecraft:testfor", "/testfor",
                "/minecraft:testforblocks", "/testforblocks",
                "/minecraft:time", "/time",
                "/minecraft:title", "/title",
                "/minecraft:toggledownfall", "/toggledownfall",
                "/minecraft:trigger", "/trigger",
                "/minecraft:tp",
                "/minecraft:whitelist",
                "/minecraft:worldborder", "/worldborder",
                "/bukkit:?", "/?",
                "/bukkit:about", "/about",
                "/bukkit:help",
                "/bukkit:version", "/version",
                "/bukkit:plugins", "/plugins",
                "/bukkit:pl", "/pl",
                "/bukkit:ver", "/ver",
                "/spigot:tps",
                "/apollo-bukkit:apollo", "/apollo",
                "/apollo-bukkit:lc", "/lc",
                "/apollo-bukkit:lunarclient", "/lunarclient",

                "/viaversion:viaversion", "/viaversion",
                "/viaversion:viaver", "/viaver",
                "/viaversion:vvbukkit", "/vvbukkit",

                "/slimeworldmanager:swm", "/swm"

        )));

        hubBlockedCommands = new HashSet<>(
                Arrays.asList(
                        "/xime:bounty", "/bounty",
                        "/xime:forcestart", "/forcestart",
                        "/xime:fs", "/fs",
                        "/xime:kill", "/kill",
                        "/xime:suicide", "/suicide",
                        "/xime:kys", "/kys",
                        "/xime:sepuku", "/sepuku",
                        "/xime:spec", "/spec",
                        "/xime:spectate", "/spectate",
                        "/xime:sponsor", "/sponsor",
                        "/xime:vote", "/vote",
                        "/xime:v", "/v",
                        "/xime:timeleft", "/timeleft",
                        "/xime:tl", "/tl"
                        ));
    }


}
