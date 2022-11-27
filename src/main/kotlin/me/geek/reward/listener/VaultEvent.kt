package me.geek.reward.listener

import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * 作者: 老廖
 * 时间: 2022/8/28
 *
 **/
class VaultEvent(val player: Player, var amount: Double, val type: ChangeType) : BukkitProxyEvent() {

    enum class ChangeType {
        ADD,
        DEL,
    }
}