package me.geek.reward.modules.task

import me.geek.GeekRewardPlus
import me.geek.GeekRewardPlus.debug
import me.geek.reward.modules.ModulesManage
import me.geek.reward.modules.ModulesManage.getPlayerData
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

/**
 * 作者: 老廖
 * 时间: 2022/8/29
 *
 **/
class OnlineTime {
    @Synchronized
    fun saveData() {
        Bukkit.getOnlinePlayers().map {
            getPlayerData(it.uniqueId)?.let { data ->
                val timeData =  (System.currentTimeMillis() / 1000) - (ModulesManage.getPlayerTimeMap(it) / 1000)
                ModulesManage.setPlayerTimeMap(it, System.currentTimeMillis())
                data.time += timeData
                debug("data.time: ${data.time}")
                ModulesManage.update(data)
            }
        }
    }
    fun calculate() {
        object : BukkitRunnable() {
            override fun run() {
                Bukkit.getOnlinePlayers().map {
                    getPlayerData(it.uniqueId)?.let { data ->
                        debug("&7当前系统时间: &8${(System.currentTimeMillis() / 1000)} &7当前玩家时间: &8${(ModulesManage.getPlayerTimeMap(it) / 1000)}")
                        val timeData =  (System.currentTimeMillis() / 1000) - (ModulesManage.getPlayerTimeMap(it) / 1000)
                        ModulesManage.setPlayerTimeMap(it, System.currentTimeMillis())
                        data.time += timeData
                        debug("data.time: ${data.time}")
                        ModulesManage.update(data)
                    }
                }
            }
        }.runTaskTimerAsynchronously(GeekRewardPlus.instance, (60 * 20), (60 * 20))
    }
}