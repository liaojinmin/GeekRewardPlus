package me.geek.reward.menu

import me.geek.reward.SetTings
import taboolib.library.xseries.XMaterial

/**
 * 作者: 老廖
 * 时间: 2022/8/19
 */
data class MenuIcon(
    /**
     * 图标字符表达式
     */
    val char: Char,

    /**
     * 图标材质
     */
    var mats: XMaterial,

    /**
     * 图标模型ID
     */
    val model: Int = 0,

    /**
     * 图标名称
     */
    var name: String,

    /**
     * 图标lore
     */
    var lore: List<String>,

    /**
     * 图标对应奖励包ID
     */
    val packID: String?,

    /**
     * 图标奖励包需要的值
     */
    val isValue: String,

    /**
     * 已领取动作
     */
    val achieve: String,

    /**
     * 不可领取动作
     */
    val deny: String,

    /**
     * 允许领取动作
     */
    val allow: String,

    /**
     * 普通的点击动作
     */
    val action: String
) {
    fun parse(value: Int, list: MutableList<String>): String {
        if (value >= isValue.toInt()) {
            return if (list.find { it == packID } != null) SetTings.State.Received.dis else SetTings.State.Available.dis
        }
        return SetTings.State.NotAvailable.dis
    }
    fun parse(value: Double, list: MutableList<String>): String {
        if (value >= isValue.toDouble()) {
            return if (list.find { it == packID } != null) SetTings.State.Received.dis else SetTings.State.Available.dis
        }
        return SetTings.State.NotAvailable.dis
    }
    fun parse(value: Long, list: MutableList<String>): String {
        if (value >= isValue.toInt()) {
            return if (list.find { it == packID } != null) SetTings.State.Received.dis else SetTings.State.Available.dis
        }
        return SetTings.State.NotAvailable.dis
    }
}