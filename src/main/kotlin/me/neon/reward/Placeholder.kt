package me.neon.reward

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.neon.reward.api.DataManager
import me.neon.reward.api.DataManager.getBasicData
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
        if (params.contains("Top")) {
            val index = params.filter { it.isDigit() }.toInt()
            return when (params) {
                "pointsTop_amt_$index" -> DataManager.boardByPointsCache[index]?.value?.toString() ?: "0"
                "pointsTop_name_$index" -> DataManager.boardByPointsCache[index]?.name ?: "暂无"
                "moneyTop_amt_$index" -> DataManager.boardByMoneyCache[index]?.value?.toString() ?: "0"
                "moneyTop_name_$index" -> DataManager.boardByMoneyCache[index]?.name ?: "暂无"
                "timeTop_amt_$index" -> DataManager.boardByTimeCache[index]?.value?.getExpiryFormat() ?: "暂无"
                "timeTop_name_$index" -> DataManager.boardByTimeCache[index]?.name ?: "暂无"
                else -> "错误参数"
            }
        }
        return when (params) {
            "points" -> player.getBasicData()?.points?.toString() ?: "0"
            "money" -> player.getBasicData()?.money?.toString() ?: "0"
            "time" -> player.getBasicData()?.time?.millis?.toString() ?: "0"
            "time_format" -> player.getBasicData()?.time?.getExpiryFormat() ?: ""
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
        return NeonRewardPlus.version
    }

    override fun persist(): Boolean {
        return true
    }
}