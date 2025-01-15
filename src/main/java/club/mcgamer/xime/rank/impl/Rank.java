package club.mcgamer.xime.rank.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @Getter
public class Rank {

    private final String name;
    private final String color = ChatColor.GREEN.toString();
    private final List<String> permissions = new ArrayList<>();

}
