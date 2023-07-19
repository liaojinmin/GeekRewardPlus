package me.geek.reward.listener

import me.geek.reward.api.DataManager
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
        val event = e.get<me.yic.xconomy.api.event.AccountEvent>()
        if (event.getisadd()) {
            val player = Bukkit.getOfflinePlayer(event.getaccountname())
            val money = event.getamount().toDouble()
            val data = DataManager.getBasicData(player.uniqueId) ?: DataManager.getBasicData(player.name!!)
            data?.let {
                it.money = ((it.money + money).toInt())
            }
        }
    }

    @SubscribeEvent(bind = "com.Zrips.CMI.events.CMIUserBalanceChangeEvent")
    fun onCMI(e: OptionalEvent) {
        val event = e.get<com.Zrips.CMI.events.CMIUserBalanceChangeEvent>()
        if (event.actionType == "Deposit") {
            val money = (event.to - event.from)
            val data = DataManager.getBasicData(event.user.uniqueId) ?: DataManager.getBasicData(event.user.name!!)
            data?.let {
                it.money += money.toInt()
            }
        }
    }

}