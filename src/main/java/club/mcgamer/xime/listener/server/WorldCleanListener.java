package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.util.IListener;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldCleanListener extends IListener {

    @EventHandler
    private void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();

        world.getLivingEntities().forEach(Entity::remove);

        world.setThunderDuration(0);
        world.setFullTime(0);
        world.setTime(6000);
        world.setStorm(false);
        world.setThundering(false);
        world.setWeatherDuration(Integer.MAX_VALUE);
        world.setMonsterSpawnLimit(-1);
        world.setAmbientSpawnLimit(-1);
        world.setAnimalSpawnLimit(-1);
        world.setTicksPerAnimalSpawns(-1);
        world.setTicksPerMonsterSpawns(-1);

        world.setGameRuleValue("commandBlockOutput", "false");
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doEntityDrops", "true");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("doMobLoot", "false");
        world.setGameRuleValue("keepInventory", "false");
        world.setGameRuleValue("mobGriefing", "false");
        world.setGameRuleValue("logAdminCommands", "false");
        world.setGameRuleValue("naturalRegeneration", "true");
        world.setGameRuleValue("reducedDebugInfo", "false");
        world.setGameRuleValue("sendCommandFeedback", "false");
        world.setGameRuleValue("showDeathMessages", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setDifficulty(Difficulty.PEACEFUL);
    }

    @EventHandler
    private void onEntitySpawn(EntitySpawnEvent event) {
        remove(event.getEntity());
    }

    @EventHandler
    private void onChunkLoad(ChunkLoadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            remove(entity);
        }
    }

    private void remove(Entity entity) {
        EntityType entityType = entity.getType();

        switch (entityType) {
            case COW:
            case PIG:
            case WOLF:
            case ARROW:
            case BLAZE:
            case GHAST:
            case GIANT:
            case HORSE:
            case SHEEP:
            case SLIME:
            case SQUID:
            case WITCH:
            case OCELOT:
            case RABBIT:
            case SPIDER:
            case WITHER:
            case ZOMBIE:
            case CHICKEN:
            case CREEPER:
            case SNOWMAN:
            case ENDERMAN:
            case GUARDIAN:
            case SKELETON:
            case VILLAGER:
            case ENDERMITE:
            case IRON_GOLEM:
            case MAGMA_CUBE:
            case CAVE_SPIDER:
            case PIG_ZOMBIE:
            case ENDER_DRAGON:
            case SILVERFISH:
            case MUSHROOM_COW:
                entity.remove();
        }
    }

}
