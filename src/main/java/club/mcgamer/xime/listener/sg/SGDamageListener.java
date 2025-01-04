package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerDamageEvent;
import club.mcgamer.xime.server.event.ServerDamageOtherEntityEvent;
import club.mcgamer.xime.server.event.ServerDeathEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SGDamageListener extends IListener {

    @EventHandler
    private void onSGDamage(ServerDamageEvent event) {
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();

            switch (serverable.getGameState()) {
                case LOBBY:
                case LOADING:
                case PREGAME:
                case PREDEATHMATCH:
                case RESTARTING:
                case CLEANUP:
                    event.getEvent().setCancelled(true);
                    break;
                case LIVEGAME:
                case DEATHMATCH:
                    if (serverable.getSpectatorList().contains(event.getVictim())
                        || (event.getAttacker().isPresent() && serverable.getSpectatorList().contains(event.getAttacker().get()))) {
                        event.getEvent().setCancelled(true);
                        break;
                    }
            }
        }
    }

    @EventHandler
    private void onSGDamageOtherEntity(ServerDamageOtherEntityEvent event) {
        if (event.getServerable() instanceof SGServerable) {

            event.getAttacker().sendMessage(event.getEvent().getEntityType().toString());

            SGServerable serverable = (SGServerable) event.getServerable();

            switch (serverable.getGameState()) {
                case LOBBY:
                case LOADING:
                case PREGAME:
                case PREDEATHMATCH:
                case RESTARTING:
                case CLEANUP:
                    event.getEvent().setCancelled(true);
                    break;
                case LIVEGAME:
                case DEATHMATCH:
                    if (serverable.getSpectatorList().contains(event.getAttacker()))
                        event.getEvent().setCancelled(true);
                    break;
            }
        }
    }

    @EventHandler
    private void onSGDeath(ServerDeathEvent event) {
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();

            Profile victim = event.getVictim();
            Player victimPlayer = victim.getPlayer();

            int tempPoints = (int) (Math.random() * 2000);
            int lostPoints = (int) (tempPoints * 0.05); //5% of a player's points

            if (victim.getServerable() == serverable)
                serverable.setSpectating(victim);
            serverable.getFallenTributes().add(victimPlayer.getDisplayName());

            victimPlayer.getWorld().strikeLightningEffect(victimPlayer.getLocation());
            victim.sendMessage("&8[&6MCSG&8] &aYou have been eliminated from the game.");

            String tributePlural = "tribute" + (serverable.getTributeList().size() == 1 ? "" : "s");
            serverable.announce(String.format("&aOnly &8[&6%s&8] &a%s remain!", serverable.getTributeList().size(), tributePlural));

            String spectatorPlural = "spectator" + (serverable.getTributeList().size() == 1 ? "" : "s");
            serverable.announce(String.format("&aThere is &8[&6%s&8] &a%s watching the game.", serverable.getSpectatorList().size(), spectatorPlural));

            victim.sendMessage(String.format("&8[&6MCSG&8] &3You've lost &8[&e%s&8] &3points for dying&8!", lostPoints));
            serverable.announceRaw(String.format("&6A cannon can be heard in the distance in memorial for %s", victim.getDisplayName()));

            TextComponent message = new TextComponent(TextUtil.translate("&fWant to join &lAnother &6Survival Games &fgame? Click "));
            TextComponent linkSection = new TextComponent(TextUtil.translate("&6&nhere&f"));
//                    serverSection.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{
//                            new TextComponent(ColorUtil.translate(String.format("&e%s &f(%s)", serverable, serverable.getPlayers().size() + " player" + (serverable.getPlayers().size() == 1 ? "" : "s")))),
//                            new TextComponent(ColorUtil.translate("\n\n&6Click to connect to this server"))
//                    }));
            linkSection.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join sg"));
            message.addExtra(linkSection);

            victim.sendMessage("");
            victim.getPlayer().spigot().sendMessage(message);
            victim.sendMessage("");

            if (event.getAttacker().isPresent()) {
                int gainedPoints = Math.max(5, lostPoints); //5 minimum points gained
                Profile attacker = event.getAttacker().get();

                attacker.sendMessage(String.format("&8[&6MCSG&8] &3You've gained &8[&e%s&8] &3points for killing %s&8!",
                        gainedPoints,
                        victim.getDisplayName()));
            }

            if (serverable.getTributeList().size() <= 1) {
                serverable.forceGameState(GameState.CLEANUP);
                return;
            }

            GameTimer gameTimer = serverable.getGameTimer();
            GameSettings gameSettings = serverable.getGameSettings();
            int currentTime = gameTimer.getCurrentTime();

            int deathmatchCountdown = 60;

            if (currentTime > deathmatchCountdown + 1 && serverable.getTributeList().size() <= gameSettings.getDeathmatchPlayers())
                gameTimer.overrideTime(deathmatchCountdown);

        }

    }

}
