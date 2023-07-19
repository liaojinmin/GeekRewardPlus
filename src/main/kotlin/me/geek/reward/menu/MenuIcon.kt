package me.geek.reward.menu

import me.geek.reward.SetTings
import org.bukkit.entity.Player
import taboolib.library.xseries.XMaterial
import taboolib.platform.compat.replacePlaceholder

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
     * 普通的点击动作
     */
    val action: String
)
