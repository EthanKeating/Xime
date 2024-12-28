package club.mcgamer.xime.rank.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor @Getter
public class Rank {

    private final String name;
    private final String color;
    private final List<String> permissions;

}
