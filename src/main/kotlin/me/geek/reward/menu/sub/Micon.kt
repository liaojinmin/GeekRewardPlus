package me.geek.reward.menu.sub

/**
 * 作者: 老廖
 * 时间: 2022/8/23
 */
interface Micon {
    val icon: String
    val mats: String
    val name: String
    val packID: String?
    val isValue: String
    var lore: List<String>
    val condition: String
    val action: String
    val deny: String
}