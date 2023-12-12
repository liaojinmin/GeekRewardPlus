package me.neon.reward.api

/**
 * @作者: 老廖
 * @时间: 2023/7/19 20:21
 * @包: me.geek.reward.api
 */
data class RewardRequire(
    val achieve: String = "",
    val deny: String= "",
    val allow: String = ""
)