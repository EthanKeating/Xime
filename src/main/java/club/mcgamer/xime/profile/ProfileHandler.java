package club.mcgamer.xime.profile;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.profile.data.impl.ProfileStatus;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProfileHandler {

    private final XimePlugin plugin;

    private final ConcurrentHashMap<UUID, Profile> profileMap = new ConcurrentHashMap<>();

    public ProfileHandler(XimePlugin plugin) {
        this.plugin = plugin;
    }

    public Profile createProfile(UUID uuid) {
        profileMap.put(uuid, new Profile(uuid, plugin));
        return profileMap.get(uuid);
    }

    public Profile getProfile(Player player) {
        if (!profileMap.containsKey(player.getUniqueId()))
            return null;

        return profileMap.get(player.getUniqueId());
    }

    public Profile getProfile(UUID uuid) {
        if (!profileMap.containsKey(uuid))
            return null;

        return profileMap.get(uuid);
    }

    public Profile removeProfile(Player player) {
        return profileMap.remove(player.getUniqueId());
    }

    public Profile removeProfile(Profile profile) {
        return profileMap.remove(profile.getUuid());
    }

    public Profile removeProfile(UUID uuid) {
        return profileMap.remove(uuid);
    }

    public Collection<Profile> getProfiles() {
        return profileMap.values().stream()
                .filter(profile -> profile.getProfileStatus() == ProfileStatus.COMPLETE)
                .collect(Collectors.toList());
    }

}
