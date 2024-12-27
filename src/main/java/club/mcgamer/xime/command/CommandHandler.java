package club.mcgamer.xime.command;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.command.map.MapCommand;
import club.mcgamer.xime.command.server.*;
import club.mcgamer.xime.command.sg.ForcestartCommand;
import club.mcgamer.xime.command.sg.SpectateCommand;
import club.mcgamer.xime.command.sg.VoteCommand;

public class CommandHandler {

    private final XimePlugin plugin;

    public CommandHandler(XimePlugin plugin) {
        this.plugin = plugin;

        new MapCommand();
        new JoinCommand();
        new HubCommand();

        new ForcestartCommand();
        new VoteCommand();
        new SpectateCommand();

        new PingCommand();
        new DisguiseCommand();
        new UndisguiseCommand();
    }




}
