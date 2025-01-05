package club.mcgamer.xime.menu.sg;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.util.Pair;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CopyOnWriteArrayList;

public class SponsorMenu extends FastInv {

    //TODO: Finish sponsor menu
    public SponsorMenu(Profile profile, Profile sponsored, SGServerable serverable) {
        super(9, TextUtil.translate("Sponsor: " + sponsored.getDisplayName()));

        int index = 0;
        for(Pair<ItemStack, Integer> sponsorItem : new CopyOnWriteArrayList<>(serverable.getSponsorItems())) {
            int cost = sponsorItem.getValue();

            ItemStack originalItem = sponsorItem.getKey();
            ItemStack displayItem = new ItemBuilder(originalItem.getType())
                    .amount(originalItem.getAmount())
                    .lore(String.format("&8[&6Cost&8] &e%s &apoints", cost))
                    .build();

            setItem(index, displayItem, e -> {

                Player clicked = ((Player) e.getWhoClicked());

                if (!serverable.getSponsorItems().contains(sponsorItem)) {
                    profile.sendMessage("&8[&6MCSG&8] &cThat item is no longer available&8.");
                    return;
                }

                serverable.getSponsorItems().remove(sponsorItem);

                //TODO: CHeck if player has points and remove them.

                sponsored.getPlayer().getInventory().addItem(originalItem);
                sponsored.getPlayer().playSound(sponsored.getPlayer().getLocation(), Sound.LEVEL_UP, 2, 2);
                sponsored.sendMessage("&8[&6MCSG&8] &fSurprise - you were sponsored by " + profile.getDisplayName());
                sponsored.sendMessage("&8[&6MCSG&8] &fRemember to thank them!");
                profile.sendMessage("&8[&6MCSG&8] &fItem sent to " + sponsored.getDisplayName());

                clicked.closeInventory();
                e.setCancelled(true);
            });
        }
    }


}
