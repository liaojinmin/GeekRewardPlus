package me.neon.reward.api.data

import java.util.UUID

/**
 * @作者: 老廖
 * @时间: 2023/7/16 17:28
 * @包: me.geek.reward.api
 */
data class BoardData<T>(
    val uuid: UUID,
    val name: String,
    val value: T,
)