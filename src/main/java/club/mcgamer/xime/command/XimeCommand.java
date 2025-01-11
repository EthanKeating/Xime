package club.mcgamer.xime.command;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;

public abstract class XimeCommand extends BukkitCommand {

    protected final XimePlugin plugin;

    public XimeCommand(String name) {
        super(name);
        this.plugin = XimePlugin.getPlugin(XimePlugin.class);
    }

    public void register() {
        ((CraftServer) Bukkit.getServer()).getCommandMap().register("xime", this);
    }

    public abstract boolean execute(CommandSender commandSender, String s, String[] strings);

    public boolean hasPermission(CommandSender commandSender) {
        if (!commandSender.hasPermission(getPermission())) {
            commandSender.sendMessage(TextUtil.translate("&cYou do not have permission to use this command!"));
            return false;
        }
        return true;
    }

    public boolean isCorrectServerable(CommandSender commandSender, Serverable serverable, Class<?>... clazz) {
        for(Class<?> loopClazz : clazz) {
            if (serverable.getClass().equals(loopClazz)) {
                return true;
            }
        }
        commandSender.sendMessage("Unknown command. Type \"/help\" for help.");
        return false;
    }

    public boolean isPlayer(CommandSender commandSender) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage(TextUtil.translate("&cYou must be a player to use this command!"));
            return false;
        }
        return true;
    }

    public Player isPlayer(CommandSender commandSender, String argument) {
        Player argumentPlayer = Bukkit.getPlayer(argument);
        if (argumentPlayer == null) {
            commandSender.sendMessage(TextUtil.translate("That player is not online!"));
        }
        return argumentPlayer;
    }

    public boolean hasArgs(CommandSender commandSender, String[] args, int argumentRequirement) {
        if (args.length < argumentRequirement) {
            commandSender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cUsage: " + getUsage()));
            return false;
        }
        return true;
    }

    public boolean hasArgs(CommandSender commandSender, String[] args, int argumentRequirement, String customPrefix) {
        if (args.length < argumentRequirement) {
            commandSender.sendMessage(TextUtil.translate(customPrefix + " &cUsage: " + getUsage()));
            return false;
        }
        return true;
    }
}
