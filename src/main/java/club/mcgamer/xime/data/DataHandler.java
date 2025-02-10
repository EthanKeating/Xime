package club.mcgamer.xime.data;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.util.TextUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.*;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class DataHandler {

    private final XimePlugin plugin;

    private final Dao<PlayerData, String> playerDataDao;
    private List<PlayerData> topPlayerData = new ArrayList<>();
    private List<PlayerData> topBGPlayerData = new ArrayList<>();

    @SneakyThrows
    public DataHandler(XimePlugin plugin) {
        this.plugin = plugin;
        String connectionString = "jdbc:mysql://"
                + plugin.getConfig().getString("mysql.host")
                + ":"
                + plugin.getConfig().getString("mysql.port")
                + "/"
                + plugin.getConfig().getString("mysql.database");

        String username = plugin.getConfig().getString("mysql.username");
        String password = plugin.getConfig().getString("mysql.password");

        ConnectionSource connectionSource = new JdbcConnectionSource(connectionString, username, password);

        TableUtils.createTableIfNotExists(connectionSource, PlayerData.class);
        playerDataDao = DaoManager.createDao(connectionSource, PlayerData.class);

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getProfileHandler().getProfiles().forEach(profile -> {
               updatePlayerData(profile.getPlayerData());
            });
            rankPlayerData();
        }, 20 * 30, 20 * 30);

    }

    @SneakyThrows
    public void createPlayerData(UUID uuid) {
        PlayerData playerData = new PlayerData();
        playerData.setUuid(uuid.toString());
        playerDataDao.create(playerData);
    }

    @SneakyThrows
    public boolean playerDataExists(UUID uuid) {
        return playerDataDao.idExists(uuid.toString());
    }

    @SneakyThrows
    public void updatePlayerData(PlayerData playerData) {
        playerDataDao.update(playerData);
    }

    @SneakyThrows
    public PlayerData getPlayerData(UUID uuid) {
        if (!playerDataExists(uuid))
            createPlayerData(uuid);

        return playerDataDao.queryForId(uuid.toString());
    }

    @SneakyThrows
    public PlayerData getPlayerData(String userName) {

        String userNameToSearch = userName.toLowerCase();

        QueryBuilder<PlayerData, String> queryBuilder = playerDataDao.queryBuilder();

        queryBuilder.where().like("userName", userNameToSearch);

        List<PlayerData> results = queryBuilder.query();

        return results.isEmpty() ? null : results.get(0);
    }

    @SneakyThrows
    public void rankPlayerData() {
        long startTime = System.currentTimeMillis();

        //Update all in database
        playerDataDao.updateRaw("SET @rank = 0");
        playerDataDao.updateRaw("UPDATE playerdata SET sgGameRank = (@rank := @rank + 1) ORDER BY sgGamesWon DESC");

        playerDataDao.updateRaw("SET @rank = 0");
        playerDataDao.updateRaw("UPDATE playerdata SET bgGameRank = (@rank := @rank + 1) ORDER BY bgKills DESC");

        //Update currently online players, so relogging doesnt override
        plugin.getProfileHandler().getProfiles().forEach(profile -> {
            PlayerData playerData = profile.getPlayerData();
            PlayerData livePlayerData = getPlayerData(profile.getUuid());

            playerData.setLastSeen(System.currentTimeMillis());
            playerData.setSgGameRank(livePlayerData.getSgGameRank());
            playerData.setBgGameRank(livePlayerData.getBgGameRank());
        });

        QueryBuilder<PlayerData, String> queryBuilder = playerDataDao.queryBuilder();

        queryBuilder.orderBy("sgGameRank", true);
        queryBuilder.limit(100L);
        topPlayerData = queryBuilder.query();

        queryBuilder = playerDataDao.queryBuilder();

        queryBuilder.orderBy("bgGameRank", true);
        queryBuilder.limit(100L);
        topBGPlayerData = queryBuilder.query();

        //Bukkit.getLogger().log(Level.INFO, "[Leaderboard] Ranked players in {0} ms", System.currentTimeMillis() - startTime);
    }
}
