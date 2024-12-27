package club.mcgamer.xime.map;

import club.mcgamer.xime.profile.Profile;
import lombok.Getter;

import java.util.*;

@Getter
public class MapPool {

    private static final Random random = new Random();

    //Where maps are fundamentally added to the sg gamemode
    private static final List<String> mapIdentifiers = Arrays.asList(
            "Par_72"
    );

    @Getter private static final HashMap<String, MapData> allMaps = new HashMap<>();

    static {
        mapIdentifiers.forEach(mapIdentifier -> allMaps.put(mapIdentifier, MapData.load(mapIdentifier)));
    }

    private final HashMap<Integer, VoteableMap> randomMaps = new HashMap<>();

    public MapPool() {
        randomizeMaps();
    }

    public void randomizeMaps() {
        Set<Integer> randomIndex = new HashSet<>();

        int mapCount = 5;

        if (mapIdentifiers.size() <= mapCount) {
            for(int i = 0; i < mapIdentifiers.size(); i++) {
                randomIndex.add(i);
            }
        }
        else {
            while (randomIndex.size() < mapCount)
                randomIndex.add(random.nextInt(mapIdentifiers.size()));
        }

        int mapInt = 1;
        for (Integer loopIndex : randomIndex) {
            String mapIdentifier = mapIdentifiers.get(loopIndex);
            MapData mapName = allMaps.get(mapIdentifier);

            System.out.println(mapIdentifier + ": "  + mapName.getMapName());

            randomMaps.put(mapInt, new VoteableMap(mapIdentifier, mapName, mapInt));
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
        return randomMaps.values().stream().max(Comparator.comparing(VoteableMap::getVotes)).get();
    }



}
