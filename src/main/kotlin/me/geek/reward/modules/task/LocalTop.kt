package me.geek.reward.modules.task

import me.geek.GeekRewardPlus
import me.geek.reward.configuration.RewardFiles
import me.geek.reward.modules.ModulesManage
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 作者: 老廖
 * 时间: 2022/8/30
 *
 **/
class LocalTop {
    val points: MutableMap<Int, String> = ConcurrentHashMap()
    val money: MutableMap<Int, String> = ConcurrentHashMap()
    val time: MutableMap<Int, String> = ConcurrentHashMap()


    fun pointsTop() {
        object : BukkitRunnable() {
            override fun run() {
                GeekRewardPlus.debug("&8运行排行榜任务")
                val var10 = ArrayList(ModulesManage.getPlayersDataMap().entries)
                if (RewardFiles.config.getBoolean("排行榜设置.充值排行榜")) {
                    var index = 0
                    var10.sortedWith { o1, o2 ->
                        o2.value.points.compareTo(o1.value.points)
                    }.forEach {
                        points[index + 1] = "${it.value.name};${it.value.points}"
                        index++
                    }
                }
                if (RewardFiles.config.getBoolean("排行榜设置.金币排行榜")) {
                    var index = 0
                    var10.sortedWith { o1, o2 ->
                        o2.value.money.compareTo(o1.value.money)
                    }.forEach {
                        money[index + 1] = "${it.value.name};${it.value.money}"
                        index++
                    }
                }
                if (RewardFiles.config.getBoolean("排行榜设置.在线排行榜")) {
                    var index = 0
                    var10.sortedWith { o1, o2 ->
                        o2.value.time.compareTo(o1.value.time)
                    }.forEach {
                        time[index + 1] = "${it.value.name};${ModulesManage.expiry.getExpiryDate(it.value.time, true)}"
                        index++
                    }
                }
            }
        }.runTaskTimerAsynchronously(GeekRewardPlus.instance,
            RewardFiles.config.getLong("排行榜设置.排行榜更新频率", 1200),
            RewardFiles.config.getLong("排行榜设置.排行榜更新频率", 1200))
    }
}