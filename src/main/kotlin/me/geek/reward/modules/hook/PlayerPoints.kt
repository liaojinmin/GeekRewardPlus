package me.geek.reward.modules.hook

import me.geek.GeekRewardPlus.say
import org.black_ixx.playerpoints.PlayerPoints
import org.black_ixx.playerpoints.PlayerPointsAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.concurrent.TimeUnit

/**
 * 作者: 老廖
 * 时间: 2022/8/28
 *
 **/
class PlayerPoints {

    private val points: PlayerPointsAPI? = Bukkit.getServer().pluginManager.getPlugin("PlayerPoints")?.let {
        say("&7软依赖 &fPlayerPoints &7已兼容.")
        (it as PlayerPoints).api
    }
    fun getPoints(player: OfflinePlayer): Int {
      return points?.lookAsync(player.uniqueId)?.get(1, TimeUnit.SECONDS) ?: -1
    }

    fun setPoints(player: OfflinePlayer, amount: Int) {
        points?.setAsync(player.uniqueId, amount)
    }

    fun hasPoints(player: OfflinePlayer, amount: Int): Boolean {
        return getPoints(player) >= amount
    }

    fun addPoints(player: OfflinePlayer, amount: Int) {
        points?.giveAsync(player.uniqueId, amount)
    }

    fun takePoints(player: OfflinePlayer, amount: Int) {
        points?.takeAsync(player.uniqueId, amount)
    }
}