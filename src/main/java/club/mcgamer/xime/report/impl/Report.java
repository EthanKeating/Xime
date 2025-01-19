package club.mcgamer.xime.report.impl;

import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
public class Report {

    private final String reportedDisplayName;
    private final String reportedName;
    private final String reportedUUID;
    private final String reportedReason;
    private final String reportDescription;
    private final String reportedDateTime;

    private final String reportPriority;
    private final String reporterDisplayName;
    private final String reporterName;
    private final String reporterUUID;

    private final ItemStack itemStack;


    public Report(Profile reported, String reportedDisplayName, String reportedName, String reportedUUID, String reportedReason, String reportDescription, String reportedDateTime, String reportPriority, String reporterDisplayName, String reporterName, String reporterUUID) {
        this.reportedDisplayName = reportedDisplayName;
        this.reportedName = reportedName;
        this.reportedUUID = reportedUUID;
        this.reportedReason = reportedReason;
        this.reportDescription = reportDescription;
        this.reportedDateTime = reportedDateTime;
        this.reportPriority = reportPriority;
        this.reporterDisplayName = reporterDisplayName;
        this.reporterName = reporterName;
        this.reporterUUID = reporterUUID;

        ItemStack tempItem = new ItemBuilder(Material.SKULL_ITEM)
                .data(3)
                .name(reportedDisplayName + " &7[" + reportPriority + "&7]")
                .lore(
                        "&7[" + reportedDateTime + "]",
                        "&8----------------------",
                        "&e&l" + reportedReason,
                        " &8* &e"  + reportDescription,
                        "&8----------------------",
                        " &bLeft Click &7to &b&lTeleport",
                        " &bRight Click&7 for more info",
                        " &bShift Click &7to &b&lClear",
                        "&8----------------------"
                        )
                .build();

        SkullMeta skullMeta = (SkullMeta) tempItem.getItemMeta();

        tempItem.setItemMeta(skullMeta);

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", reported.getSkin().getValue(), reported.getSkin().getSignature()));

        try {

            Field field = skullMeta.getClass().getDeclaredField("profile"); // We get the field profile.

            field.setAccessible(true); // We set as accessible to modify.
            field.set(skullMeta, gameProfile); // We set in the skullMeta the modified GameProfile that we created.

        } catch (Exception e) {
            e.printStackTrace();
        }


        tempItem.setItemMeta(skullMeta);
        itemStack = tempItem;
    }
}
