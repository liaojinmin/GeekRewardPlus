package me.geek.reward.listener

import me.geek.GeekRewardPlus
import me.geek.reward.modules.ModulesManage
import org.black_ixx.playerpoints.event.PlayerPointsChangeEvent
import org.bukkit.Bukkit
import taboolib.common.platform.event.SubscribeEvent


/**
 * 作者: 老廖
 * 时间: 2022/8/19
 *
 **/
object PointsListener {


    @SubscribeEvent
    fun onChange(e: PlayerPointsChangeEvent) {
        val points = e.change
        if (points >= 0) {
            ModulesManage.getPlayerData(e.playerId)?.let {
                if (it.points <= 0) {
                    it.points = points
                } else {
                    it.points+=points
                }
                Bukkit.getScheduler().scheduleAsyncDelayedTask(GeekRewardPlus.instance) {
                    ModulesManage.update(it)
                }
            }
        }
    }

}