package club.mcgamer.xime.sg.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.loot.LootTable;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.ChestUtil;
import club.mcgamer.xime.util.Pair;
import org.bukkit.Difficulty;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;

public class LiveGameRunnable extends AbstractGameRunnable {

    private final XimePlugin plugin;
    private final SGServerable serverable;
    private final GameTimer gameTimer;

    public LiveGameRunnable(SGServerable serverable, XimePlugin plugin) {
        this.plugin = plugin;
        this.serverable = serverable;
        this.gameTimer = serverable.getGameTimer();

        GameSettings gameSettings = serverable.getGameSettings();

        gameTimer.setTime(gameSettings.getLiveGameLength());

        fillChests(true);
        serverable.setJoinable(true);
        serverable.announce("&3The games have begun!");
        serverable.announceTitle("", "" , 0, 30, 0);
        serverable.getWorld().setDifficulty(Difficulty.EASY);
//        serverable.getTributeList().forEach(profile -> {
//            profile.getPlayer().setMaxHealth(gameSettings.getMaxHealth());
//            profile.getPlayer().setHealth(gameSettings.getMaxHealth());
//        });
    }

    public void run() {
        GameSettings gameSettings = serverable.getGameSettings();
        int currentTime = gameTimer.decrement();

        int minutes = gameTimer.getMinutes();
        int seconds = gameTimer.getSeconds();

        Pair<String, String> sigUnit = gameTimer.toSignificantUnit();

        //Switch to predeathmatch
        if (currentTime == 0) {
            cancel();
            return;
        }

        if ((seconds == 0 && minutes > 0 && (minutes % 5 == 0 || minutes < 5)) || (minutes == 0 && (seconds % 30 == 0 || seconds == 10 || seconds <= 5))) {
            serverable.announceTitle("", String.format("&8[&e%s&8] &c%s until deathmatch!",
                    sigUnit.getKey(),
                    sigUnit.getValue()), 10, 80, 10);
            serverable.announce(String.format("&8[&e%s&8] &c%s until deathmatch!",
                    sigUnit.getKey(),
                    sigUnit.getValue()));
        }

        if ((minutes == 17 || minutes == 4) && seconds == 0) {
            fillChests(true);
            serverable.announceSound(Sound.CHEST_OPEN, 1, 1);
            serverable.announceTitle("", "&3Sponsors have refilled the chests!", 10, 80, 10);
            serverable.announce("&3Sponsors have refilled the chests!");
            if (!serverable.getFallenTributes().isEmpty())
                serverable.announce("&fThese tributes have passed: " + String.join("&f, ", serverable.getFallenTributes()));
        }
    }

    public void cancel() {
        super.cancel();

        serverable.setGameState(GameState.PREDEATHMATCH);
    }

    private void fillChests(boolean clearInventory) {
        MapData mapData = serverable.getMapData();
        LootTable loot = serverable.getGameSettings().getLootTable();
        World world = serverable.getWorld();

        serverable.getOpenedChestLocations().clear();

        mapData.getTier2Locations()
                .stream()
                .map(mapLocation -> mapLocation.toBukkit(world))
                .forEach(worldLocation -> {
                    try {
                        Block block = world.getBlockAt(worldLocation);
                        BlockState blockState = block.getState();
                        byte rawData = blockState.getRawData();
                        blockState.setTypeId(54);
                        blockState.setRawData(rawData);
                        blockState.update(true);
                    } catch (Exception ex) {
                        System.out.println("[Chest Error]: " + worldLocation.getBlockX() + ", " + worldLocation.getBlockY() + ", " + worldLocation.getBlockZ());
                    }
                });

        mapData.getTier2Locations()
                .stream()
                .map(mapLocation -> mapLocation.toBukkit(world))
                .forEach(worldLocation -> {
                    try {
                        Block block = world.getBlockAt(worldLocation);
                        BlockState blockState = block.getState();
                        byte rawData = blockState.getRawData();
                        blockState.setTypeId(54);
                        blockState.setRawData(rawData);
                        blockState.update(true);
                        Chest chestState = ((Chest) worldLocation.getBlock().getState());

                        if (clearInventory) chestState.getBlockInventory().clear();
                        chestState.update(true);

                        loot.fill(chestState, LootTable.Tier.TWO, serverable.getGameSettings().getLootStyle());
                        switch (serverable.getGameSettings().getLootStyle()) {
                            case MIXED_ITEMS:
                            case MIXED_CHESTS:
                                break;
                            case INVERTED:
                            case ONLY_TIER_1:
                                ChestUtil.rename(worldLocation, "Tier I");
                                break;
                            case ONLY_TIER_2:
                            case DEFAULT:
                            default:
                                ChestUtil.rename(worldLocation, "Tier II");
                                break;
                        }
                    } catch (Exception ex) {
                        System.out.println("[Chest Error]: " + worldLocation.getBlockX() + ", " + worldLocation.getBlockY() + ", " + worldLocation.getBlockZ());
                    }
                });
        mapData.getTier1Locations()
                .stream()
                .map(mapLocation -> mapLocation.toBukkit(world))
                .forEach(worldLocation -> {
                    try {
                        Chest chestState = (Chest) (worldLocation.getBlock().getState());
                        if (clearInventory) chestState.getBlockInventory().clear();
                        chestState.update(true);

                        loot.fill(chestState, LootTable.Tier.ONE, serverable.getGameSettings().getLootStyle());
                        switch (serverable.getGameSettings().getLootStyle()) {
                            case MIXED_CHESTS:
                                break;
                            case INVERTED:
                            case ONLY_TIER_2:
                                ChestUtil.rename(worldLocation, "Tier II");
                                break;
                            case ONLY_TIER_1:
                            case DEFAULT:
                            default:
                                ChestUtil.rename(worldLocation, "Tier I");
                                break;
                        }
                    } catch (Exception ex) {
                        System.out.println("[Chest Error]: " + worldLocation.getBlockX() + ", " + worldLocation.getBlockY() + ", " + worldLocation.getBlockZ());
                    }
                });
    }

}
