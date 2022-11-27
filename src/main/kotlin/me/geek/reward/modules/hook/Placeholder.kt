package me.geek.reward.modules.hook

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.geek.GeekRewardPlus
import me.geek.reward.modules.ModulesManage
import me.geek.reward.modules.ModulesManage.getPlayerData
import org.bukkit.entity.Player

/**
 * 作者: 老廖
 * 时间: 2022/8/28
 */
class Placeholder : PlaceholderExpansion() {
    // %GeekReward_points% int
    // %GeekReward_money% double
    // %GeekReward_time% Long
    // %GeekReward_time_format% 00d 00h 00m 00s

    // %GeekReward_pointsTop_amt_1%
    // %GeekReward_pointsTop_name_1%
    // %GeekReward_moneyTop_amt_1%
    // %GeekReward_moneyTop_name_1%
    // %GeekReward_timeTop_amt_1%
    // %GeekReward_timeTop_name_1%
    override fun onPlaceholderRequest(player: Player, params: String): String {
        val uuid = player.uniqueId
        if (params.contains("Top")) {
            val index = params.filter { it.isDigit() }.toInt()
            return when (params) {
                "pointsTop_amt_$index" -> GeekRewardPlus.top.points[index]?.let {
                    it.split(";")[1]
                } ?: "暂无"
                "pointsTop_name_$index" -> GeekRewardPlus.top.points[index]?.let {
                    it.split(";")[0]
                } ?: "暂无"
                "moneyTop_amt_$index" -> GeekRewardPlus.top.money[index]?.let {
                    it.split(";")[1]
                } ?: "暂无"
                "moneyTop_name_$index" -> GeekRewardPlus.top.money[index]?.let {
                    it.split(";")[0]
                } ?: "暂无"
                "timeTop_amt_$index" -> GeekRewardPlus.top.time[index]?.let {
                    it.split(";")[1]
                } ?: "暂无"
                "timeTop_name_$index" -> GeekRewardPlus.top.time[index]?.let {
                    it.split(";")[0]
                } ?: "暂无"
                else -> "错误参数"
            }
        }
        return when (params) {
            "points" -> getPlayerData(uuid)?.points.toString()
            "money" -> getPlayerData(uuid)?.money.toString()
            "time" -> getPlayerData(uuid)?.time.toString()
            "time_format" -> ModulesManage.expiry.getExpiryDate(getPlayerData(uuid).time, true)
            else -> "null"
        }
    }

    override fun getIdentifier(): String {
        return "GeekReward"
    }

    override fun getAuthor(): String {
        return "极客天上工作室"
    }

    override fun getVersion(): String {
        return GeekRewardPlus.version
    }
}