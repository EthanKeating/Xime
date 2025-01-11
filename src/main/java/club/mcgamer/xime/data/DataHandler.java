package club.mcgamer.xime.data;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.data.entities.PlayerData;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

@Getter
public class DataHandler {

    private final XimePlugin plugin;

    private final Dao<PlayerData, String> playerDataDao;

    private String connectionString = "jdbc:mysql://mysql-1.gb-lhr.game.heavynode.net:3306/s16081_mcgamer_playerdata";
    private String username = "u16081_i5gNiX83Yy";
    private String password = "lA9qJ+R=nDvDHxHI^zJ=84sz";

    //private List<PlayerData> topPlayerData = new ArrayList<>();

    @SneakyThrows
    public DataHandler(XimePlugin plugin) {
        this.plugin = plugin;
        ConnectionSource connectionSource = new JdbcConnectionSource(connectionString, username, password);

        TableUtils.createTableIfNotExists(connectionSource, PlayerData.class);
        playerDataDao = DaoManager.createDao(connectionSource, PlayerData.class);

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getProfileHandler().getProfiles().forEach(profile -> {
               updatePlayerData(profile.getPlayerData());
            });
            rankPlayerData();
        }, 60 * 20, 60 * 20);

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
    public void rankPlayerData() {
        long startTime = System.currentTimeMillis();

        //Update all in database
        playerDataDao.updateRaw("SET @rank = 0");
        playerDataDao.updateRaw("UPDATE playerdata SET sgGameRank = (@rank := @rank + 1) ORDER BY sgGamesWon DESC");

        //Update currently online players, so relogging doesnt override
        plugin.getProfileHandler().getProfiles().forEach(profile -> {
            PlayerData playerData = profile.getPlayerData();
            PlayerData livePlayerData = getPlayerData(profile.getUuid());

            playerData.setSgGameRank(livePlayerData.getSgGameRank());
        });
//
//        QueryBuilder<PlayerData, String> queryBuilder = playerDataDao.queryBuilder();
//
//        queryBuilder.orderBy("sgGameRank", false);
//        queryBuilder.limit(100L);
//        topPlayerData = queryBuilder.query();

        //Bukkit.getLogger().log(Level.INFO, "[Leaderboard] Ranked players in {0} ms", System.currentTimeMillis() - startTime);
    }
}
