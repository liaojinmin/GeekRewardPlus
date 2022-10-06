package me.geek.reward.listener

import me.geek.GeekRewardPlus
import me.geek.reward.modules.ModulesManage
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
        // 储存玩家上线时间
        ModulesManage.setPlayerTimeMap(player, System.currentTimeMillis())
        //判断玩家是否有缓存数据，无则查询或添加默认数据
        if (ModulesManage.getPlayerData(player.uniqueId) == null) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(GeekRewardPlus.instance) {
                val data = ModulesManage.select(player)
                if (data != null) {
                    ModulesManage.setPlayerData(player.uniqueId, data)
                }
            }
        }
    }
    @SubscribeEvent
    fun onJoin(e: PlayerQuitEvent) {
        val player = e.player
        ModulesManage.getPlayerData(player.uniqueId)?.let {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(GeekRewardPlus.instance) {
                // 计算在线时间
                val timeData = (System.currentTimeMillis() / 1000) - (ModulesManage.getPlayerTimeMap(player) / 1000)
                it.time += timeData
                // 更新数据
                ModulesManage.update(it)
                ModulesManage.remPlayerTimeMap(player)
            }
        }
    }
}