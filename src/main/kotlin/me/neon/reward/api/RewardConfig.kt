package me.neon.reward.api

import me.neon.reward.SetTings.State.*
import me.neon.reward.api.data.ExpIryBuilder

/**
 * @作者: 老廖
 * @时间: 2023/7/19 20:20
 * @包: me.geek.reward.api
 */
data class RewardConfig<T>(
    val id: String,
    val priority: Int = 0,
    val value: T,
    val info: List<String> = mutableListOf(),
    val require: RewardRequire = RewardRequire()
) {


    fun parse(pID: MutableList<String>, pValue: T): String {
        if (pValue is Int && value is Int) {
            // pValue == Int
            // value ????
            if (pValue >= value) {
                return if (pID.find {  it == id } != null) Received.dis else Available.dis
            }
            return NotAvailable.dis
        }
        if (pValue is ExpIryBuilder && value is ExpIryBuilder) {
            if (pValue.millis >= value.millis) {
                return if (pID.find {  it == id } != null) Received.dis else Available.dis
            }
            return NotAvailable.dis
        }
        return "null"
    }

    fun parseValue(): String {
        return if (value is ExpIryBuilder) value.getExpiryFormat() else value.toString()
    }
    fun parseValue(pValue: T): String {
        return if (pValue is ExpIryBuilder) pValue.getExpiryFormat() else pValue.toString()
    }
}
