package me.geek.reward.menu.sub

import me.geek.reward.utils.HexUtils
import org.bukkit.Material
import taboolib.library.configuration.ConfigurationSection

/**
 * 作者: 老廖
 * 时间: 2022/8/19
 */
data class Icon(
    override val icon: String,
    override var mats: String,
    override var name: String,
    override val packID: String?,
    override val isValue: String,
    override val condition: String,
    override var lore: List<String>,
    override val action: String,
    override val deny: String
) : Micon {
    constructor(icon: String, obj: ConfigurationSection) : this(
        icon,
        obj.getString("display.mats", Material.STONE.name)!!,
        HexUtils.colorify(obj.getString("display.name", " ")!!),
        obj.getString("display.packID", null),
        obj.getString("display.isValue","0")!!,
        obj.getString("Require.condition", "false")!!,
        HexUtils.colorify(obj.getStringList("display.lore").joinToString()).split(", "),
        obj.getString("Require.action", "false")!!,
        obj.getString("Require.deny", "true")!!
    )

}