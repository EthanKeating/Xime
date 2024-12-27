package club.mcgamer.xime.command;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.command.map.MapCommand;
import club.mcgamer.xime.command.server.HubCommand;
import club.mcgamer.xime.command.server.JoinCommand;
import club.mcgamer.xime.command.sg.SpectateCommand;

public class CommandHandler {

    private final XimePlugin plugin;

    public CommandHandler(XimePlugin plugin) {
        this.plugin = plugin;

        new MapCommand();
        new JoinCommand();
        new HubCommand();
        new SpectateCommand();
    }




}
