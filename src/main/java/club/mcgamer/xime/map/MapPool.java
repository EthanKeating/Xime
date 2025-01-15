package club.mcgamer.xime.map;

import club.mcgamer.xime.profile.Profile;
import lombok.Getter;

import java.util.*;

@Getter
public class MapPool {

    private static final Random random = new Random();

    //Where maps are fundamentally added to the sg gamemode
    @Getter private static final List<String> mapIdentifiers = Arrays.asList(
            "Par_72",
            "Green_Grass",
            "Demons_Breeze",
            "Icarus_Fallen",
            "Valleyside_University",
            "Survival_Games_4",
            "Alaskan_Village",
            "Breeze_Island",
            "Breeze_Island_2",
            "Chernobyl_(2015)",
            "Avaricia",
            "Pompeii",
            "Zone_85_(Revamped)",
            "Turbulence",
            "Solar_Frost",
            "Inertia",
            "Survival_Kingdom",
            "Construct",
            "Excavation_Zero",
            "Fortress_Pyke",
            "Highfield_Estate",
            "Holiday_Resort",
            "Holiday_Resort_2",
            "Origins_(Reqrium_V2)",
            "Winds_of_Change",
            "Lobby_Games"
            //"Return_To_Moonbase_9"
    );

    @Getter private static final HashMap<String, MapData> allMaps = new HashMap<>();

    static {
        mapIdentifiers.forEach(mapIdentifier -> allMaps.put(mapIdentifier, MapData.load(mapIdentifier)));
    }

    private final HashMap<Integer, VoteableMap> randomMaps = new HashMap<>();
    private final boolean mapChances = true;

    public MapPool() {
        randomizeMaps();
    }

    public void setMap(String mapIdentifier) {
        randomMaps.clear();

        randomMaps.put(0, new VoteableMap(mapIdentifier, allMaps.get(mapIdentifier), 0));
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
            MapData mapData = allMaps.get(mapIdentifier);

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

    public static VoteableMap get(String mapIdentifier) {
        if (!mapIdentifiers.contains(mapIdentifier))
            return null;

        return new VoteableMap(mapIdentifier, allMaps.get(mapIdentifier), 0);
    };



}
