package club.mcgamer.xime.util;


import club.mcgamer.xime.loot.LootTable;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

//I DID NOT WRITE THIS SHIT, ITS FROM TESC0S SHITTY ASS PLUGIN BUT IM FORCED TO USE HIS CODE FOR THIS
public class FireworkUtil
{
    public static void sendFirework(final Location location) {
        final Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        final FireworkMeta fwm = fw.getFireworkMeta();
        final int rt = LootTable.random.nextInt(5) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 2) {
            type = FireworkEffect.Type.BALL_LARGE;
        }
        if (rt == 3) {
            type = FireworkEffect.Type.BURST;
        }
        if (rt == 4) {
            type = FireworkEffect.Type.CREEPER;
        }
        if (rt == 5) {
            type = FireworkEffect.Type.STAR;
        }
        final int r1i = LootTable.random.nextInt(17) + 1;
        final int r2i = LootTable.random.nextInt(17) + 1;
        final Color c1 = getColor(r1i);
        final Color c2 = getColor(r2i);
        final FireworkEffect effect = FireworkEffect.builder().flicker(LootTable.random.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(LootTable.random.nextBoolean()).build();
        fwm.addEffect(effect);
        final int rp = LootTable.random.nextInt(2) + 1;
        fwm.setPower(rp);
        fw.setFireworkMeta(fwm);
    }

    private static Color getColor(final int i) {
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }
        return c;
    }
}
