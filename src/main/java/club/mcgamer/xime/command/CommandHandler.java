package club.mcgamer.xime.command;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.command.map.MapCommand;
import club.mcgamer.xime.command.server.*;
import club.mcgamer.xime.command.sg.*;

public class CommandHandler {

    private final XimePlugin plugin;

    public CommandHandler(XimePlugin plugin) {
        this.plugin = plugin;

        new WhitelistCommand();
        new MapCommand();
        new JoinCommand();
        new HubCommand();

        new ForcestartCommand();
        new VoteCommand();
        new SpectateCommand();
        new TimeLeftCommand();
        new KillCommand();

        new PingCommand();
        new DisguiseCommand();
        new UndisguiseCommand();
    }




}
