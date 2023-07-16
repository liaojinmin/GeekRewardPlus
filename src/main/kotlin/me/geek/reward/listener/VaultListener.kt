package me.geek.reward.listener

import me.yic.xconomy.api.event.AccountEvent
import com.Zrips.CMI.events.CMIUserBalanceChangeEvent
import me.geek.reward.api.DataManager

import org.bukkit.Bukkit
import taboolib.common.platform.event.SubscribeEvent

/**
 * 作者: 老廖
 * 时间: 2022/8/28
 **/
object VaultListener {

    @SubscribeEvent
    fun onChange(e: AccountEvent) {
        if (e.getisadd()) {
            val player = Bukkit.getOfflinePlayer(e.getaccountname())
            val money = e.getamount().toDouble()
            val data = DataManager.getBasicData(player.uniqueId) ?: DataManager.getBasicData(player.name!!)
            data?.let {
                it.money = (it.money + money)
            }
        }
    }

    @SubscribeEvent
    fun onChange(e: CMIUserBalanceChangeEvent) {
        if (e.actionType == "Deposit") {
            val money = (e.to - e.from)
            val data = DataManager.getBasicData(e.user.uniqueId) ?: DataManager.getBasicData(e.user.name!!)
            data?.let {
                it.money += money
            }
        }
    }
}