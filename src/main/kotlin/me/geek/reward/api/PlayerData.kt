package me.geek.reward.api

import com.google.gson.GsonBuilder
import me.geek.GeekRewardPlus
import me.geek.reward.SetTings
import me.geek.reward.service.Exclude
import java.util.UUID


/**
 * 作者: 老廖
 * 时间: 2022/8/18
 */
class PlayerData(
    val uuid: UUID,
    val name: String,
) {

    var points: Int = 0

    var money: Double = 0.0

    var time: ExpIryBuilder = ExpIryBuilder("", false)

    var pointsKey: MutableList<String> = mutableListOf()
        private set

    var moneyKey: MutableList<String> = mutableListOf()
        private set

    var timeKey: MutableList<String> = mutableListOf()
        private set


    fun reset() {
        this.money = 0.0
        this.points = 0
        this.time = ExpIryBuilder("", false)
        this.pointsKey = mutableListOf()
        this.moneyKey = mutableListOf()
        this.timeKey = mutableListOf()
    }

    fun toByteArray(): ByteArray = toJson().toByteArray(Charsets.UTF_8)

    private fun toJson(): String {
        val data = GsonBuilder()
            .setExclusionStrategies(Exclude())
            .create()
            .toJson(this)
        parse(data)
        return data
    }
    private fun parse(text: String) {
        if (SetTings.deBug) {
            GeekRewardPlus.debug(text)
        }
    }


}