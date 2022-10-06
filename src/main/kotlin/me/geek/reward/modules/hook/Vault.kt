package me.geek.reward.modules.hook

import me.geek.GeekRewardPlus.say
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

/**
 * 作者: 老廖
 * 时间: 2022/8/28
 *
 **/
class Vault {
    private val moneys: Economy? = if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
        say("&7软依赖 &fVault &7已兼容.")
        Bukkit.getServer().servicesManager.getRegistration(Economy::class.java)?.provider
    } else null

    fun takeMoney(player: OfflinePlayer, money: Double) {
        moneys?.withdrawPlayer(player, money)
    }

    fun addMoney(player: OfflinePlayer, money: Double) {
        moneys?.depositPlayer(player, money)
    }

    fun hasMoney(player: OfflinePlayer, money: Double): Boolean {
        return moneys?.has(player, money) ?: false
    }

    fun getMoney(player: OfflinePlayer): Double {
        return moneys?.getBalance(player) ?: 0.0
    }

    fun setMoney(player: OfflinePlayer, money: Double) {
        addMoney(player, money - getMoney(player))
    }
}