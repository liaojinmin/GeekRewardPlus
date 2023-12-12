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
 * 时间: 2022/8/28
 **/
object VaultListener {

    @SubscribeEvent(bind = "me.yic.xconomy.api.event.AccountEvent")
    fun onXC(e: OptionalEvent) {
        if (SetTings.setConfig.closeMoney) return
        val event = e.get<me.yic.xconomy.api.event.AccountEvent>()
        if (event.getisadd()) {
            val player = Bukkit.getOfflinePlayer(event.getaccountname())
            val money = event.getamount().toDouble()
            val data = DataManager.getBasicData(player.uniqueId) ?: DataManager.getBasicData(player.name!!)
            data?.let {
                it.money = ((it.money + money).toInt())
                it.updateByTask()
            }
        }
    }

    @SubscribeEvent(bind = "com.Zrips.CMI.events.CMIUserBalanceChangeEvent")
    fun onCMI(e: OptionalEvent) {
        if (SetTings.setConfig.closeMoney) return
        val event = e.get<com.Zrips.CMI.events.CMIUserBalanceChangeEvent>()
        if (event.actionType == "Deposit") {
            val money = (event.to - event.from)
            val data = DataManager.getBasicData(event.user.uniqueId) ?: DataManager.getBasicData(event.user.name!!)
            data?.let {
                it.money += money.toInt()
                it.updateByTask()
            }
        }
    }

    @SubscribeEvent(bind = "me.geek.vault.api.event.PlayerMoneyChangeEvent")
    fun onGeek(e: OptionalEvent) {
        if (SetTings.setConfig.closeMoney) return
        val event = e.get<me.geek.vault.api.event.PlayerMoneyChangeEvent>()
        if (event.eventAction == EventAction.GIVE) {
            val money = event.new - event.old
            val data = DataManager.getBasicData(event.data.getPlayerUUID()) ?: DataManager.getBasicData(event.data.getPlayerNAME())
            data?.let {
                it.money += money
                it.updateByTask()
            }
        }
    }

}