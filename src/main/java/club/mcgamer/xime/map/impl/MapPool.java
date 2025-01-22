package club.mcgamer.xime.map.impl;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.map.MapHandler;
import club.mcgamer.xime.profile.Profile;
import lombok.Getter;

import java.util.*;

@Getter
public class MapPool {


    private static final Random random = new Random();

    private final HashMap<Integer, VoteableMap> randomMaps = new HashMap<>();
    private final boolean mapChances = true;

    private final XimePlugin plugin;
    private final MapHandler mapHandler;

    public MapPool(XimePlugin plugin) {
        this.plugin = plugin;
        this.mapHandler = plugin.getMapHandler();

        randomizeMaps();
    }

    public void setMap(String mapIdentifier) {
        randomMaps.clear();

        MapData mapData = mapHandler.getMapPool().get(mapIdentifier);

        randomMaps.put(0, new VoteableMap(mapIdentifier, mapData, 0));
    }

    public void randomizeMaps() {
        Set<Integer> randomIndex = new HashSet<>();

        int mapCount = 5;

        if (mapHandler.getActiveMaps().size() <= mapCount) {
            for(int i = 0; i < mapHandler.getActiveMaps().size(); i++) {
                randomIndex.add(i);
            }
        }
        else {
            while (randomIndex.size() < mapCount)
                randomIndex.add(random.nextInt(mapHandler.getActiveMaps().size()));
        }

        List<String> mapIdentifiers = new ArrayList<>(mapHandler.getActiveMaps());

        int mapInt = 1;
        for (Integer loopIndex : randomIndex) {
            String mapIdentifier = mapIdentifiers.get(loopIndex);
            MapData mapData = mapHandler.getMapPool().get(mapIdentifier);

            randomMaps.put(mapInt, new VoteableMap(mapIdentifier, mapData, mapInt));
            mapInt++;
        }
    }

    public void addVote(Profile profile, int mapInt) {
        removeVote(profile);

        randomMaps.get(mapInt).getVotedFor().add(profile);
    }

    public void removeVote(Profile profile) {
        randomMaps.values().forEach(voteableMap -> voteableMap.getVotedFor().remove(profile));
    }

    public int getVote(Profile profile) {
        Optional<VoteableMap> votedMap = randomMaps.values().stream().filter(voteableMap -> voteableMap.getVotedFor().contains(profile)).findAny();
        return votedMap.map(VoteableMap::getMapIndex).orElse(-1);
    }

    public VoteableMap complete() {
        if (isMapChances()) {
            int totalVotes = randomMaps.values().stream()
                    .mapToInt(VoteableMap::getVotes)
                    .sum();

            double rand = random.nextDouble() * 100;

            double cumulativeChance = 0;
            for(VoteableMap voteableMap : getRandomMaps().values()) {
                double chancePercentage = (double) voteableMap.getVotes() / totalVotes * 100;
                cumulativeChance += chancePercentage;

                if (rand < cumulativeChance) {
                    return voteableMap;
                }
            }
        }

        return randomMaps.values().stream().max(Comparator.comparing(VoteableMap::getVotes)).get();
    }



}
