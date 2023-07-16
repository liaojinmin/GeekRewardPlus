package me.geek.reward.listener

import me.geek.GeekRewardPlus
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * 作者: 老廖
 * 时间: 2022/8/28
 *
 **/
object PlayerListener {

    @SubscribeEvent
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player
        //判断玩家是否有缓存数据，无则查询或添加默认数据
        if (ModulesManage.getPlayerData(player.name) == null) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(GeekRewardPlus.instance) {
                val data = ModulesManage.select(player)
                if (data != null) {
                    ModulesManage.setPlayerData(player.name, data)
                }
            }
        }
    }

    @SubscribeEvent
    fun onQuit(e: PlayerQuitEvent) {
        val player = e.player
        ModulesManage.getPlayerData(player.name)?.let {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(GeekRewardPlus.instance) {
                // 计算在线时间
                val timeData = (System.currentTimeMillis() / 1000) - (ModulesManage.getPlayerTimeMap(player) / 1000)
                it.time += timeData
                // 更新数据
                ModulesManage.update(it)
                ModulesManage.remPlayerTimeMap(player)
            }
        }
        ModulesManage.remPlayerData(player.name)
    }
}