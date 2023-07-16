package me.geek.reward.api

import java.util.concurrent.ConcurrentHashMap

/**
 * @作者: 老廖
 * @时间: 2023/7/16 17:26
 * @包: me.geek.reward.service
 */
object BoardManager {

    val cache: MutableMap<Int, BoardData> = ConcurrentHashMap()




}