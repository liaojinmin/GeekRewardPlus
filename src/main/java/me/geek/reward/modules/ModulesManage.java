package me.geek.reward.modules;

import me.geek.GeekRewardPlus;
import me.geek.reward.configuration.ConfigManager;
import me.geek.reward.database.DataManage;
import me.geek.reward.modules.hook.ItemsAdder;
import me.geek.reward.modules.hook.PlayerPoints;
import me.geek.reward.modules.hook.Vault;
import me.geek.reward.modules.hook.Placeholder;
import me.geek.reward.utils.Expiry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 作者: 老廖
 * 时间: 2022/8/25
 **/
public final class ModulesManage {
    // 玩家UUID ， data
    private final static Map<UUID, PlayersData> PLAYERS_DATA_MAP = new ConcurrentHashMap<>();
    private final static Map<Player, Long> PLAYER_TIME_MAP = new ConcurrentHashMap<>();
    private final static Plugin plugin = GeekRewardPlus.INSTANCE.getInstance();
    public final static Expiry expiry = new Expiry();

    public static boolean hasTotalPoints(@NotNull UUID uuid, int points) {
        return PLAYERS_DATA_MAP.containsKey(uuid) && PLAYERS_DATA_MAP.get(uuid).getPoints() >= points;
    }
    public static boolean hasTotalMoney(@NotNull UUID uuid, double money) {
        return PLAYERS_DATA_MAP.containsKey(uuid) && PLAYERS_DATA_MAP.get(uuid).getMoney() >= money;
    }

    public static PlayersData getPlayerData(@NotNull UUID uuid) {
        return PLAYERS_DATA_MAP.getOrDefault(uuid, null);
    }

    public static void setPlayerData(@NotNull UUID uuid, @NotNull PlayersData data) {
        PLAYERS_DATA_MAP.put(uuid, data);
    }
    public static void remPlayerData(@NotNull UUID uuid) {
        if (PLAYERS_DATA_MAP.get(uuid)!=null) {
            PLAYERS_DATA_MAP.remove(uuid);
        }
    }

    /**
     * 获取玩家登录时的时间戳
     * @param player 目标玩家
     * @return 时间戳
     */
    public static long getPlayerTimeMap(@NotNull Player player) {
      return PLAYER_TIME_MAP.get(player);
    }
    public static void setPlayerTimeMap(@NotNull Player player, long time) {
        PLAYER_TIME_MAP.put(player, time);
    }
    public static void remPlayerTimeMap(@NotNull Player player) {
        PLAYER_TIME_MAP.remove(player);
    }

    public static Map<UUID, PlayersData> getPlayersDataMap() {
        return PLAYERS_DATA_MAP;
    }

    public static PlayerPoints points;
    public static Vault vault;
    public static ItemsAdder itemsAdder;
    public static void onStart() {
        itemsAdder = new ItemsAdder();
        vault = new Vault();
        points = new PlayerPoints();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            GeekRewardPlus.say("&7软依赖 &fPlaceholderAPI &7已兼容.");
            new Placeholder().register();
        }
    }

    public static void insert(@NotNull PlayersData playersData) {
        try (Connection connection = DataManage.getConnection()) {
            try (PreparedStatement p = connection.prepareStatement(
                    "INSERT INTO " + ConfigManager.MYSQL_Player_Data_NAME + "(`name`,`uuid`,`points`,`money`,`time`,`points_key`,`money_key`,`time_key`) VALUES(?,?,?,?,?,?,?,?);")) {
                p.setString(1, playersData.getName());
                p.setString(2, playersData.getUuid().toString());
                p.setInt(3, playersData.getPoints());
                p.setDouble(4, playersData.getMoney());
                p.setLong(5, playersData.getTime());
                p.setString(6, Arrays.toString(playersData.getPoints_key()));
                p.setString(7, Arrays.toString(playersData.getMoney_key()));
                p.setString(8, Arrays.toString(playersData.getTime_key()));
                p.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void update(@NotNull PlayersData playersData) {
        try (Connection connection = DataManage.getConnection()) {
            try (PreparedStatement p = connection.prepareStatement(
                    "UPDATE " + ConfigManager.MYSQL_Player_Data_NAME + " SET `points`=?,`money`=?,`time`=?,`points_key`=?,`money_key`=?,`time_key`=? WHERE `uuid`=?;")) {
                p.setInt(1, playersData.getPoints());
                p.setDouble(2, playersData.getMoney());
                p.setLong(3,playersData.getTime());
                p.setString(4, Arrays.toString(playersData.getPoints_key()).replace("[, ","["));
                p.setString(5, Arrays.toString(playersData.getMoney_key()).replace("[, ","["));
                p.setString(6, Arrays.toString(playersData.getTime_key()).replace("[, ","["));
                p.setString(7, playersData.getUuid().toString());
                p.execute();
                GeekRewardPlus.debug("&8update-完毕,dataID= "+playersData.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询玩家数据，如果数据库中不存在则插入默认数据
     * @param player 目标玩家
     * @return 返回玩家数据，如果不存在则返回默认数据
     */
    public static PlayersData select(@NotNull Player player) {
        PlayersData playerData = null;
        try (Connection connection = DataManage.getConnection()) {
            try (PreparedStatement p = connection.prepareStatement(
                    "SELECT * FROM " + ConfigManager.MYSQL_Player_Data_NAME + " WHERE uuid=?;")) {
                final UUID uuid = player.getUniqueId();
                p.setString(1, String.valueOf(uuid));
                ResultSet res = p.executeQuery();
                if (!res.isBeforeFirst()) {
                    playerData = PlayersData.Companion.DEFAULT_PLAYER_DATA(uuid, player.getName());
                    insert(playerData);
                    return playerData;
                }
                while (res.next()) {
                    final String name = res.getString("name");
                    final int points = res.getInt("points");
                    final double money = res.getDouble("money");
                    final int time = res.getInt("time");
                    final String[] points_key = res.getString("points_key")
                            .replace("[", "")
                            .replace("]", "")
                            .replace(" ","")
                            .split(",");
                    final String[] money_key = res.getString("money_key")
                            .replace("[", "")
                            .replace("]", "")
                            .replace(" ","")
                            .split(",");
                    final String[] time_key = res.getString("time_key")
                            .replace("[", "")
                            .replace("]", "")
                            .replace(" ","")
                            .split(",");
                    playerData = new PlayersData(name, uuid, points, money, time, points_key, money_key, time_key);
                }
                return playerData;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerData;
    }
    public static void getPlayerData() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection c = DataManage.getConnection()) {
                    try (Statement s = c.createStatement()) {
                        ResultSet res = s.executeQuery("SELECT * FROM " + ConfigManager.MYSQL_Player_Data_NAME + " WHERE id");
                        if (!res.isBeforeFirst()) {
                            return;
                        }
                        PLAYERS_DATA_MAP.clear();
                        while (res.next()) {
                            final String name = res.getString("name");
                            final UUID uuid = UUID.fromString(res.getString("uuid"));
                            final int points = res.getInt("points");
                            final double money = res.getDouble("money");
                            final int time = res.getInt("time");
                            final String[] points_key = res.getString("points_key")
                                    .replace("[", "")
                                    .replace("]", "")
                                    .replace(" ","")
                                    .split(",");
                            final String[] money_key = res.getString("money_key")
                                    .replace("[", "")
                                    .replace("]", "")
                                    .replace(" ","")
                                    .split(",");
                            final String[] time_key = res.getString("time_key")
                                    .replace("[", "")
                                    .replace("]", "")
                                    .replace(" ","")
                                    .split(",");
                            PLAYERS_DATA_MAP.put(uuid, new PlayersData(name, uuid, points, money, time, points_key, money_key, time_key));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public static void insertMigrator(List<PlayersData> playerData) {
        try (Connection connection = DataManage.getConnection()) {
            try (PreparedStatement p = connection.prepareStatement(
                    "INSERT INTO "+ ConfigManager.MYSQL_Player_Data_NAME +"(`name`,`uuid`,`points`,`money`,`time`,`points_key`,`money_key`,`time_key`) VALUES(?,?,?,?,?,?,?,?);")) {
                for (PlayersData data : playerData) {
                    p.setString(1, data.getName());
                    p.setString(2, data.getUuid().toString());
                    p.setInt(3, data.getPoints());
                    p.setDouble(4, data.getMoney());
                    p.setLong(5, data.getTime());
                    p.setString(6, Arrays.toString(data.getPoints_key()));
                    p.setString(7, Arrays.toString(data.getMoney_key()));
                    p.setString(8, Arrays.toString(data.getTime_key()));
                    p.addBatch();
                }
                p.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
