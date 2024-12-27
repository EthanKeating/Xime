package club.mcgamer.xime.util;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntityChest;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftChest;

import java.lang.reflect.Field;

@UtilityClass
public class ChestUtil {

    public void rename(Location loc, String name) {
        try {
            Field inventoryField = CraftChest.class.getDeclaredField("chest");
            inventoryField.setAccessible(true);
            TileEntityChest teChest = ((TileEntityChest) inventoryField.get(loc.getBlock().getState()));

            if ((name.equals("Tier I") && teChest.getContainerName().equals("Tier II")) || (name.equals("Tier II") && teChest.getContainerName().equals("Tier I")))
                teChest.a("Tier I/II");
            else
                teChest.a(name);
            teChest.update();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void playChestAction(Chest chest, boolean open) {
        Location location = chest.getLocation();
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());
        TileEntityChest tileChest = (TileEntityChest) world.getTileEntity(position);
        world.playBlockAction(position, tileChest.w(), 1, open ? 1 : 0);
    }

}
