package club.mcgamer.xime.profile;

import club.mcgamer.xime.XimePlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class ProfileHandler {

    private final XimePlugin plugin;

    @Getter private final HashMap<UUID, Profile> profileMap = new HashMap<>();

    public ProfileHandler(XimePlugin plugin) {
        this.plugin = plugin;
    }

    public Profile createProfile(Player player) {
        profileMap.put(player.getUniqueId(), new Profile(player.getUniqueId()));
        return profileMap.get(player.getUniqueId());
    }

    public Profile getProfile(Player player) {
        if (!profileMap.containsKey(player.getUniqueId()))
            return createProfile(player);

        return profileMap.get(player.getUniqueId());
    }

    public Profile getProfile(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null)
            return null;

        return getProfile(player);
    }

    public Profile removeProfile(Player player) {
        return profileMap.remove(player.getUniqueId());
    }

    public Profile removeProfile(Profile profile) {
        return removeProfile(profile.getPlayer());
    }

    public Collection<Profile> getProfiles() {
        return profileMap.values();
    }

}
