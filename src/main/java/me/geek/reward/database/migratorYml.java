package me.geek.reward.database;

import me.geek.GeekRewardPlus;
import me.geek.reward.modules.ModulesManage;
import me.geek.reward.modules.PlayersData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

/**
 * 作者: 老廖
 * 时间: 2022/10/21
 **/
public class migratorYml {

    // 玩家名称 , data
    private final Map<String, Integer> cache = new HashMap<>();

    // 点券礼包键 ， 玩家名称
    private final Map<String, List<String>> pointsCache = new HashMap<>();


    private final List<PlayersData> playerData = new ArrayList<>();
    private File file;

    public migratorYml(String name) {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(GeekRewardPlus.INSTANCE.getInstance(), () -> {
            GeekRewardPlus.say(" &7正在准备Yml迁移器...");
            file = new File(GeekRewardPlus.INSTANCE.getInstance().getDataFolder(), name);
            select_player();
            select_server();
            Classification();
        });
    }
    private void select_player() {
        GeekRewardPlus.say(" &7获取玩家数据...");
        List<File> var1 = new ArrayList<>();
        ForFile(file, var1);
        for (File fileOut : var1) {
            String playerName = fileOut.getName();
            GeekRewardPlus.say("获取玩家名称: "+playerName);
            FileConfiguration data = YamlConfiguration.loadConfiguration(new File(fileOut, "data.yml"));
            int points = data.getInt("Points");
            cache.put(playerName, points);
        }
    }


    private void select_server() {
        GeekRewardPlus.say(" &7获取奖励数据...");
        File f = new File(GeekRewardPlus.INSTANCE.getInstance().getDataFolder(), "data.yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);
        Object[] obj = data.getConfigurationSection("Data.Points").getKeys(false).toArray();
        for (Object o : obj) {
            GeekRewardPlus.say(o.toString());
            List<String> list = data.getStringList("Data.Points."+o);
            pointsCache.put(o.toString(), list);
            GeekRewardPlus.say("礼包键值: "+o);
            for (String out : list) {
                GeekRewardPlus.say("玩家名称: "+out);
            }
        }
    }
    private void Classification() {
        GeekRewardPlus.say(" &a转移玩家数据中...");
        cache.forEach((playerName, value) -> {
            GeekRewardPlus.debug("转储: "+playerName);
            List<String> points = new ArrayList<>();
            if (value != 0) {
                pointsCache.forEach((pack, values) -> {
                    boolean core = false;
                    for (String name : values) {
                        if (playerName.equals(name) && !core) {
                            GeekRewardPlus.debug("匹配玩家名: "+playerName +" 点券数量: "+pack);
                            core = true;
                            points.add(pack);
                        }
                    }
                });
            }
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            GeekRewardPlus.debug("添加数据...");
            playerData.add(new PlayersData(player.getName(), player.getUniqueId(), value, 0,0,
                            points.toArray(new String[0]), new String[0], new String[0]
                    ));
        });
        ModulesManage.insertMigrator(this.playerData);
        GeekRewardPlus.say(" &a迁移完成...");
    }










    private void ForFile(File var1, List<File> var2) {
        if (var1.isDirectory()) {
            File[] amt = var1.listFiles();
            if (amt != null) {
                var2.addAll(Arrays.asList(amt));
            }
        }
    }

}
