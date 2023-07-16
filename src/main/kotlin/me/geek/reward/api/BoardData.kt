package me.geek.reward.api

import java.util.UUID

/**
 * @作者: 老廖
 * @时间: 2023/7/16 17:28
 * @包: me.geek.reward.api
 */
data class BoardData(
    val uuid: UUID,
    val name: String,
    val time: String,
    val money: String,
    val points: String
)