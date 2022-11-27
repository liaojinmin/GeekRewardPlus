package me.geek.reward.listener

import me.geek.reward.modules.ModulesManage
import me.yic.xconomy.api.event.AccountEvent
import com.Zrips.CMI.events.CMIUserBalanceChangeEvent

import org.bukkit.Bukkit
import taboolib.common.platform.event.SubscribeEvent

/**
 * 作者: 老廖
 * 时间: 2022/8/28
 *
 **/
object VaultListener {

    @SubscribeEvent
    fun onChange(e: AccountEvent) {
        if (e.getisadd()) {
            Bukkit.getPlayer(e.getaccountname())?.let {
                val uuid = it.uniqueId
                val money = e.getamount().toDouble()
                ModulesManage.getPlayerData(uuid)?.let { data ->
                    data.money = (data.money + money)
                }
            }
        }
    }
    @SubscribeEvent
    fun onChange(e: CMIUserBalanceChangeEvent) {
        if (e.actionType == "Deposit") {
            val uuid = e.user.getUuid()
            val money = (e.to - e.getFrom())
            ModulesManage.getPlayerData(uuid)?.let {
                it.money = (it.money + money)
            }
        }
    }
}