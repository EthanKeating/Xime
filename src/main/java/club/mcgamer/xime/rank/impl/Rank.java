package club.mcgamer.xime.rank.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @Getter
public class Rank {

    private final String name;
    private String color = ChatColor.GREEN.toString();
    private List<String> permissions = new ArrayList<>();

}
