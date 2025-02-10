package club.mcgamer.xime.listener.sgmaker;

import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.menu.sgmaker.TeamSettingsMenu;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SGMakerJoinListener extends IListener {

    @EventHandler
    private void onSGJoin(ServerJoinEvent event) {
        if (event.getServerable() instanceof SGMakerServerable) {
            SGMakerServerable serverable = (SGMakerServerable) event.getServerable();
            Profile profile = event.getProfile();
            Player player = profile.getPlayer();
            String prefix = serverable.getPrefix();

            GameState gameState = serverable.getGameState();

            profile.sendTitle("", "", 10, 10, 10);

            if (gameState == GameState.LOBBY && profile == serverable.getOwner()) {
                profile.sendMessage(prefix + "&eWelcome to your custom MCSG game!");
                profile.sendMessage("&6You can configure the game using the &anether star &6in your inventory&8.");

                TextComponent inviteMessage = new TextComponent(TextUtil.translate("&6You can invite people to join by using the command &a/invite <player>"));
                inviteMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{
                        new TextComponent(TextUtil.translate("&a/invite <player>" + " &7(Click to use)")),
                }));
                inviteMessage.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/invite "));
                player.spigot().sendMessage(inviteMessage);

                TextComponent message = new TextComponent(TextUtil.translate("&6Players can manually join using &a&nthis&6 secret code &7(Hover)"));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{
                        new TextComponent(TextUtil.translate("&a/secret " + serverable.getSecret() + " &7(Click to copy)")),
                }));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/secret " + serverable.getSecret()));
                player.spigot().sendMessage(message);

                player.getInventory().setItem(4, new ItemBuilder(Material.NETHER_STAR).name("&bServer Management").build());
            }

            if (gameState == GameState.LOBBY) {
                if (serverable.getGameSettings().getTeamProvider().getTeamType() != TeamType.NO_TEAMS) {
                    player.getInventory().setItem(0, TeamSettingsMenu.TEAM_ITEM);
                }
            }

        }
    }
}
