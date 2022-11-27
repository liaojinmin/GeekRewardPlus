package me.geek.reward.modules

import java.util.UUID

/**
 * 作者: 老廖
 * 时间: 2022/8/18
 */
class PlayersData
    (
    val name: String,
    var uuid: UUID,
    var points: Int,
    var money: Double,
    var time: Long,
    var Points_key: Array<String>,
    var Money_key: Array<String>,
    var Time_key: Array<String>
    )
{
    fun reset() {
        this.money = 0.0
        this.points = 0
        this.time = 0
        this.Points_key = emptyArray()
        this.Money_key = emptyArray()
        this.Time_key = emptyArray()
    }

    companion object {
        fun DEFAULT_PLAYER_DATA(uuid: UUID, name: String): PlayersData {
            return PlayersData(name, uuid, 0, 0.00, 0, arrayOf(""), arrayOf(""), arrayOf(""))
        }
    }

}