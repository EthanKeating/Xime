package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerDamageEvent;
import club.mcgamer.xime.server.event.ServerDamageOtherEntityEvent;
import club.mcgamer.xime.server.event.ServerDeathEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

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
                case ENDGAME:
                case CLEANUP:
                    event.getEvent().setCancelled(true);
                    return;
                case LIVEGAME:
                case DEATHMATCH:

                    if ((event.getAttacker().isPresent() && serverable.getSpectatorList().contains(event.getAttacker().get()))) {
                        if (event.getEvent().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                            event.getEvent().setCancelled(true);
                        }
                        if (serverable.getSpectatorList().contains(event.getVictim()))
                            event.getEvent().setCancelled(true);
                        return;
                    }

                    if (serverable.getSpectatorList().contains(event.getVictim())) {
                        event.getEvent().setCancelled(true);
                    }
            }

            if (event.getAttacker().isPresent()) {
                if (event.getEvent().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getAttacker().get().getPlayer().getInventory().getItemInHand().getType() == Material.AIR) {
                    event.getEvent().setDamage(Math.min(0.5, event.getEvent().getDamage()));
                }
            }
        }
    }

    @EventHandler
    private void onCombust(EntityCombustEvent event) {
        if (event.getEntity() instanceof Player player) {
            Profile profile = plugin.getProfileHandler().getProfile(player);
            if (profile == null) return;

            if (profile.getServerable() instanceof SGServerable serverable) {
                if (serverable.getSpectatorList().contains(profile)) {
                    event.setDuration(0);
                    event.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    private void onPainting(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player player) {
            Profile profile = plugin.getProfileHandler().getProfile(player);
            if (profile.getServerable() instanceof SGServerable serverable) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onSGDamageOtherEntity(ServerDamageOtherEntityEvent event) {
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
                    return;
            }
        }
    }

    @EventHandler
    private void onSGDeath(ServerDeathEvent event) {
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();
            String prefix = serverable.getPrefix();

            if (serverable.getGameState() == GameState.LOBBY ||serverable.getGameState() == GameState.ENDGAME || serverable.getGameState() == GameState.CLEANUP) {
                event.getVictim().getPlayer().setMaxHealth(20);
                event.getVictim().getPlayer().setHealth(20);
                return;
            }

            Profile victim = event.getVictim();
            Player victimPlayer = victim.getPlayer();

            if (victimPlayer.isDead()) victimPlayer.setHealth(20);
            PlayerData victimData = victim.getPlayerData();
            SGTemporaryData victimTempData = (SGTemporaryData) victim.getTemporaryData();

            //int tempPoints = (int) (Math.random() * 2000);
            int lostPoints = (int) (victimData.getSgPoints() * 0.05); //5% of a player's points

            if (!(serverable instanceof SGMakerServerable)) {
                victimData.setSgPoints(Math.max(0, victimData.getSgPoints() - lostPoints));
                victimData.setSgDeaths(victimData.getSgDeaths() + 1);
                victimData.setSgGamesPlayed(victimData.getSgGamesPlayed() + 1);

                long lifeDuration = System.currentTimeMillis() - victimTempData.getLifeStart();

                victimData.setSgLifeSpan(victimData.getSgLifeSpan() + lifeDuration);
                if (victimData.getSgLongestLifeSpan() < lifeDuration)
                    victimData.setSgLongestLifeSpan(lifeDuration);
            }

            if (victim.getServerable() == serverable) {
                if ((victimPlayer.getLastDamageCause() != null && victimPlayer.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) || victimPlayer.getLocation().getY() < 0) {
                    victimPlayer.teleport(serverable.getMapData().getCenterLocation().toBukkit(victimPlayer.getWorld()));
                }
                serverable.setSpectating(victim);
            }
            serverable.getFallenTributes().add(victimPlayer.getDisplayName());

            victimPlayer.getWorld().strikeLightningEffect(victimPlayer.getLocation().add(0, 8, 0));
            victim.sendMessage(prefix + "&aYou have been eliminated from the game.");

            String tributePlural = "tribute" + (serverable.getTributeList().size() == 1 ? "" : "s");
            serverable.announce(String.format("&aOnly &8[&6%s&8] &a%s remain!", serverable.getTributeList().size(), tributePlural));

            String spectatorPlural = "spectator" + (serverable.getTributeList().size() == 1 ? "" : "s");
            serverable.announce(String.format("&aThere is &8[&6%s&8] &a%s watching the game.", serverable.getSpectatorList().size(), spectatorPlural));

            if (!(serverable instanceof SGMakerServerable))
                victim.sendMessage(String.format(prefix + "&3You've lost &8[&e%s&8] &3points for dying&8!", lostPoints));
            victim.sendTitle("&cYou have died.", "", 10, 50, 10);

            if (event.getVictim().getTemporaryData() instanceof SGTemporaryData) {
                SGTemporaryData temporaryData = (SGTemporaryData) event.getVictim().getTemporaryData();

                if (!(serverable instanceof SGMakerServerable)) {
                    if (temporaryData.getBounty() > 0) {
                        if (event.getAttacker().isPresent()) {
                            Profile attacker = event.getAttacker().get();
                            PlayerData attackerData = attacker.getPlayerData();

                            attackerData.setSgPoints(attackerData.getSgPoints() + temporaryData.getBounty());
                            event.getAttacker().get().sendMessage(String.format(prefix + "&3You've gained &8[&e%s&8] &3extra points from bounties set on &f%s&8!", temporaryData.getBounty(), event.getVictim().getDisplayName()));
                        }
                        serverable.announceRaw(String.format("&6A bounty of &8[&a%s&8] &6points has been claimed upon &f%s&6's death&8.", temporaryData.getBounty(), event.getVictim().getDisplayName()));
                        temporaryData.setBounty(0);
                    }
                }
            }

            if (event.getAttacker().isPresent()) {
                int gainedPoints = Math.max(5, lostPoints); //5 minimum points gained
                Profile attacker = event.getAttacker().get();

                PlayerData attackerData = attacker.getPlayerData();
                SGTemporaryData temporaryAttackerData = (SGTemporaryData) event.getVictim().getTemporaryData();

                if (!(serverable instanceof SGMakerServerable)) {
                    attackerData.setSgPoints(attackerData.getSgPoints() + gainedPoints);
                    attackerData.setSgKills(attackerData.getSgKills() + 1);
                    temporaryAttackerData.setKillCount(temporaryAttackerData.getKillCount() + 1);
                    if (temporaryAttackerData.getKillCount() > attackerData.getSgMostKills())
                        attackerData.setSgMostKills(temporaryAttackerData.getKillCount());

                    attacker.sendMessage(String.format(prefix + "&3You've gained &8[&e%s&8] &3points for killing %s&8!",
                            gainedPoints,
                            victim.getDisplayName()));
                }
            }

            serverable.announceRaw(String.format("&6A cannon can be heard in the distance in memorial for %s", victim.getDisplayName()));

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                TextComponent message = new TextComponent(TextUtil.translate("&fWant to join &lAnother &6MCSG &fgame? Click &f&nHere&f!"));
//                    serverSection.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{
//                            new TextComponent(ColorUtil.translate(String.format("&e%s &f(%s)", serverable, serverable.getPlayers().size() + " player" + (serverable.getPlayers().size() == 1 ? "" : "s")))),
//                            new TextComponent(ColorUtil.translate("\n\n&6Click to connect to this server"))
//                    }));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join sg"));

                if (victimPlayer.isOnline()) {
                    victim.sendMessage("");
                    victim.getPlayer().spigot().sendMessage(message);
                    victim.sendMessage("");
                }
            }, 1);

            if (serverable.getTributeList().size() <= 1) {
                serverable.forceGameState(GameState.ENDGAME);
                return;
            }

            GameTimer gameTimer = serverable.getGameTimer();
            GameSettings gameSettings = serverable.getGameSettings();
            int currentTime = gameTimer.getCurrentTime();

            int deathmatchCountdown = 60;

            if ((serverable.getGameState() != GameState.PREDEATHMATCH && serverable.getGameState() != GameState.DEATHMATCH) && currentTime > deathmatchCountdown + 1 && serverable.getTributeList().size() <= gameSettings.getDeathmatchPlayers())
                gameTimer.overrideTime(deathmatchCountdown);

        }

    }

}
