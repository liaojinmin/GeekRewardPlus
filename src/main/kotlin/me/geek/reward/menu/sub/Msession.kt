package me.geek.reward.menu.sub

import org.bukkit.inventory.ItemStack

/**
 * 作者: 老廖
 * 时间: 2022/8/23
 */
interface Msession {
    val session: String
    val title: String
    val stringLayout: String
    val size: Int
    val bindings: String
    val type: String
    var micon: List<Micon>
    var itemStack: Array<ItemStack>
}