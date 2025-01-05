package club.mcgamer.xime.menu.sg;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.entity.Player;

public class SponsorMenu extends FastInv {

    //TODO: Finish sponsor menu
    public SponsorMenu(Profile profile, Profile sponsored) {
        super(9, TextUtil.translate("Sponsor: " + sponsored.getDisplayName()));
//
//        setItem(0, serverItem, e -> {
//
//            sponsored.getPlayer().getInventory().addItem(new Mater)
//            ((Player) e.getWhoClicked()).performCommand("hub " + server.getServerId());
//            e.setCancelled(true);
//        });
    }


}
