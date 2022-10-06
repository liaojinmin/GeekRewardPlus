package me.geek.reward.api;

import me.geek.reward.modules.ModulesManage;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * 作者: 老廖
 * 时间: 2022/8/28
 **/
public class PlayersDataApi {
    public static Double getPlayerMoney(@NotNull UUID uuid) {
        return (ModulesManage.getPlayerData(uuid) != null) ? ModulesManage.getPlayerData(uuid).getMoney() : 0.00;
    }
    public static int getPlayerPoints(@NotNull UUID uuid) {
        return (ModulesManage.getPlayerData(uuid) != null) ? ModulesManage.getPlayerData(uuid).getPoints() : 0;
    }
    public static long getPlayerTime(@NotNull UUID uuid) {
        return (ModulesManage.getPlayerData(uuid) != null) ? ModulesManage.getPlayerData(uuid).getTime() : 0L;
    }
}
