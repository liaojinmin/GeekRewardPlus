package me.neon.reward.listener

import me.neon.reward.SetTings
import me.neon.reward.api.DataManager
import me.neon.reward.api.DataManager.updateByTask
import me.geek.vault.api.event.EventAction
import org.bukkit.Bukkit
import taboolib.common.platform.event.OptionalEvent
import taboolib.common.platform.event.SubscribeEvent


/**
 * 作者: 老廖
 * 时间: 2022/8/19
 *
 **/
object PointsListener {


    @SubscribeEvent(bind = "org.black_ixx.playerpoints.event.PlayerPointsChangeEvent")
    fun onChange(e: OptionalEvent) {
        if (SetTings.setConfig.closePoints) return
        val event = e.get<org.black_ixx.playerpoints.event.PlayerPointsChangeEvent>()
        val player = Bukkit.getOfflinePlayer(event.playerId)
        val points = event.change
        if (points >= 0) {
            val data = DataManager.getBasicData(player.uniqueId) ?: DataManager.getBasicData(player.name!!)
            data?.let {
                if (it.points <= 0) {
                    it.points = points
                } else {
                    it.points = (it.points + points)
                }
                it.updateByTask()
            }
        }
    }

    @SubscribeEvent(bind = "me.geek.vault.api.event.PlayerPointsChangeEvent")
    fun onGeek(e: OptionalEvent) {
        if (SetTings.setConfig.closePoints) return
        val event = e.get<me.geek.vault.api.event.PlayerPointsChangeEvent>()
        if (event.eventAction == EventAction.GIVE) {
            val points = event.new - event.old
            val data = DataManager.getBasicData(event.data.getPlayerUUID()) ?: DataManager.getBasicData(event.data.getPlayerNAME())
            data?.let {
                it.points += points
                it.updateByTask()
            }
        }
    }
}