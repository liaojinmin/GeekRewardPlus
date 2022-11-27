package me.geek.reward.database;


import me.geek.reward.configuration.ConfigManager;
import me.geek.GeekRewardPlus;
import me.geek.reward.modules.ModulesManage;
import me.geek.reward.modules.PlayersData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;


import java.sql.*;
import java.util.*;



/**
 * 作者: 老廖
 * 时间: 2022/8/18
 **/
public final class migrator {
    // 玩家名称 , data
    private final Map<String, migratorData> cache = new HashMap<>();

    // 点券礼包键 ， 玩家名称
    private final Map<String, List<String>> pointsCache = new HashMap<>();
    // 金币礼包键 ， 玩家名称
    private final Map<String, List<String>> moneyCache = new HashMap<>();
    // 时间礼包键 ， 玩家名称
    private final Map<String, List<String>> timeCache = new HashMap<>();

    private final List<PlayersData> playerData = new ArrayList<>();


    public Connection connection = null;
    public Statement statement = null;

    public migrator(String database) {
        GeekRewardPlus.say(" &7正在准备迁移器...");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + ConfigManager.MYSQL_HOST + ":" + ConfigManager.MYSQL_PORT + "/" + database + ConfigManager.MYSQL_PARAMS, ConfigManager.MYSQL_USERNAME, ConfigManager.MYSQL_PASSWORD);
        } catch (SQLException s) {
            s.printStackTrace();
        }
        select_player();
        select_server();
        int size = cache.size() + pointsCache.size() + moneyCache.size() + timeCache.size() + playerData.size();
        progressBar(size);
        Classification();
    }


    private void select_player() {
        GeekRewardPlus.say(" &7获取玩家数据...");
        try {
            statement = connection.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM `pmreward_player`");
            if (!res.isBeforeFirst()) {
                res.close();
                statement.close();
                return;
            }
            while (res.next()) {
                final String name = res.getString("player");
                final int points = res.getInt("points");
                final double money = res.getDouble("money");
                final int time = res.getInt("time");
                cache.put(name, new migratorData(points, money, time));
            }
            res.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void select_server() {
        GeekRewardPlus.say(" &7获取奖励数据...");
        try {
            statement = connection.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM `pmreward_server`");
            if (!res.isBeforeFirst()) {
                res.close();
                statement.close();
                return;
            }
            final YamlConfiguration yaml = new YamlConfiguration();
            while (res.next()) {
                final String points = res.getObject("points").toString();
                final String money = res.getObject("money").toString();
                final String time = res.getObject("time").toString();
                // 加载点券
                yaml.loadFromString(points);
                Object[] objects = yaml.getConfigurationSection("points").getKeys(false).toArray();
                for (Object o : objects) {
                    pointsCache.put(o.toString(), yaml.getStringList("points." + o));
                }
                // 加载金币
                yaml.loadFromString(money);
                Object[] var10 = yaml.getConfigurationSection("money").getKeys(false).toArray();
                for (Object o : var10) {
                    moneyCache.put(o.toString(), yaml.getStringList("money." + o));
                }
                // 加载时间
                yaml.loadFromString(time);
                Object[] var100 = yaml.getConfigurationSection("time").getKeys(false).toArray();
                for (Object o : var100) {
                    timeCache.put(o.toString(), yaml.getStringList("time." + o));
                }
            }
            res.close();
            statement.close();
        } catch (SQLException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    private void Classification() {
        // key = 玩家名称 ， value = data
        cache.forEach((playerName, value) -> {
            List<String> points = new ArrayList<>();
            List<String> money = new ArrayList<>();
            List<String> time = new ArrayList<>();
            //   GeekRewardPro.say("玩家名称key: "+ key + " - 玩家充值数量: "+ value);
            if (value.getPoints() != 0) {
                pointsCache.forEach((pack, values) -> {
                    //    GeekRewardPro.say("礼包值pack: "+ key + " - 玩家名称: "+ value);
                    boolean core = false;
                    for (String name : values) {
                        if (playerName.equals(name) && !core) {
                            //  GeekRewardPro.say("匹配到玩家名称: "+ name + " 与 礼包值pack: "+ pack + " 吻合");
                            core = true;
                            points.add(pack);
                        }
                    }
                });
            }

            if (value.getMoney() != 0) {
                moneyCache.forEach((pack, values) -> {
                    boolean core = false;
                    for (String name : values) {
                        if (playerName.equals(name) && !core) {
                            core = true;
                            money.add(pack);
                        }
                    }
                });
            }
            if (value.getTime() != 0) {
                timeCache.forEach((pack, values) -> {
                    boolean core = false;
                    for (String name : values) {
                        if (playerName.equals(name) && !core) {
                            core = true;
                            time.add(pack);
                        }
                    }
                });
            }
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            playerData.add(new PlayersData(playerName, player.getUniqueId(), value.getPoints(), value.getMoney(), value.getTime(),
                    points.toArray(new String[0]),
                    money.toArray(new String[0]),
                    time.toArray(new String[0])));
        });
        ModulesManage.insertMigrator(this.playerData);
        this.cache.clear();
        this.pointsCache.clear();
        this.moneyCache.clear();
        this.timeCache.clear();
        this.playerData.clear();
        if (connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        GeekRewardPlus.say(" &a迁移完成...");
    }

    private void progressBar(int speedNum) {
        char incomplete = '-';
        char complete = '=';
        int total = 100;
        Random rd = new Random();
        StringBuilder sb = new StringBuilder();
//        Stream.generate(() -> incomplete).limit(total).forEach(sb::append);
        for (int i = 0; i < total; i++) {
            sb.append(incomplete);
        }
        for (int i = 0; i < total; i++) {
            sb.replace(i, i + 1, String.valueOf(complete));
            String progressBar = "\r" + sb;
            String percent = " " + (i + 1) + "%";
            System.out.print(progressBar + percent);
            // Bukkit.getConsoleSender().sendRawMessage(progressBar + percent);
            try {
                Thread.sleep((long) rd.nextInt(2) * speedNum / 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class migratorData {
    private final int Points;
    private final double money;
    private final int time;

    public migratorData(int points, double money, int time) {
        this.Points = points;
        this.money = money;
        this.time = time;
    }

    public int getPoints() {
        return Points;
    }

    public double getMoney() {
        return money;
    }

    public int getTime() {
        return time;
    }
}
