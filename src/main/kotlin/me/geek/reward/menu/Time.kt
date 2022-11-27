package me.geek.reward.menu

import me.geek.GeekRewardPlus
import me.geek.reward.configuration.ConfigManager
import me.geek.reward.kether.sub.KetherAPI
import me.geek.reward.menu.sub.Micon
import me.geek.reward.menu.sub.Msession
import me.geek.reward.modules.ModulesManage
import me.geek.reward.modules.ModulesManage.expiry
import me.geek.reward.modules.PlayersData
import me.geek.reward.utils.colorify
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import taboolib.platform.compat.replacePlaceholder
import java.util.ArrayList
import kotlin.system.measureTimeMillis

/**
 * 作者: 老廖
 * 时间: 2022/8/29
 *
 **/
class Time(
    private val player: Player,
    private val session: Msession,
    private val inventory: Inventory
) {
    private val playerData: PlayersData = ModulesManage.getPlayerData(player.uniqueId)!!
    private val iCon: List<Micon> = session.micon

    init {
        measureTimeMillis {
            build()
            actions()
        }.also {
            GeekRewardPlus.debug("&8加载菜单 &7${session.session} &8耗时 &7$it &8Ms")
        }
    }
    private fun actions() {
        Menu.isOpen.add(player)
        player.openInventory(inventory)
        Bukkit.getPluginManager().registerEvents(object : Listener {
            var start = System.currentTimeMillis()
            @EventHandler
            fun onClick(event: InventoryClickEvent) {
                if (event.view.player !== player) {
                    return
                }
                if (event.rawSlot < 0 || event.currentItem == null) {
                    event.isCancelled = true
                    return
                }
                event.isCancelled = true
                if (start < System.currentTimeMillis()) {
                    start = System.currentTimeMillis() + 300
                    if (event.rawSlot > session.stringLayout.length) return
                    val id = session.stringLayout[event.rawSlot].toString()

                    for (icon in iCon) {
                        if (icon.icon == id && icon.packID != null) {
                            GeekRewardPlus.debug("&8条件: ${icon.condition}")
                            if (KetherAPI.instantKether(player, icon.condition).asBoolean() && playerData.time >= expiry.setExpiryMillis(icon.isValue, false)) {

                                KetherAPI.instantKether(
                                    player, icon.action.colorify()
                                        .replace("{player_name}", player.name)
                                        .replace("{player_uuid}", player.uniqueId.toString())
                                )
                                Bukkit.getScheduler().scheduleSyncDelayedTask(GeekRewardPlus.instance) {
                                    val packID = icon.packID
                                    if (playerData.Time_key.isNotEmpty()) {
                                        val list: MutableList<String> = ArrayList(listOf(*playerData.Time_key))
                                        list.add(packID!!)
                                        val key  = list.toTypedArray()
                                        playerData.Time_key = key
                                        ModulesManage.update(playerData)
                                    } else {
                                        playerData.Time_key = arrayOf(packID!!)
                                        ModulesManage.update(playerData)
                                    }
                                }
                            } else {
                                GeekRewardPlus.debug("&8玩家 ${player.name} 尝试获取 ${icon.packID} 未达成条件")
                                KetherAPI.instantKether(player, icon.deny.colorify())
                                return
                            }
                        }
                    }
                }
            }

            @EventHandler
            fun onDrag(event: InventoryDragEvent) {
                if (event.view.player === player) {
                    event.isCancelled = true
                }
            }

            @EventHandler
            fun onClose(event: InventoryCloseEvent) {
                player.updateInventory()
                if (event.view.player === player) {
                    HandlerList.unregisterAll(this)
                    Menu.isOpen.removeIf { it === player }
                }
            }
        }, GeekRewardPlus.instance)
    }

    private fun build() {
        for ((index, value) in session.stringLayout.withIndex()) {
            if (value != ' ') {
                iCon.forEach { micon ->
                    if (micon.icon[0] == value) {
                        val itemMeta = inventory.contents[index].itemMeta
                        if (itemMeta != null) {
                            if (itemMeta.hasLore()) {
                                var lores = itemMeta.lore!!.joinToString()
                                lores = if (playerData.Time_key.contains(micon.packID)) {
                                    lores.replace("{state}", ConfigManager.OK, ignoreCase = true)
                                } else if (playerData.time >= expiry.setExpiryMillis(micon.isValue, false)) {
                                    lores.replace("{state}", ConfigManager.YES, ignoreCase = true)
                                } else {
                                    lores.replace("{state}", ConfigManager.NO, ignoreCase = true)
                                }
                                lores = lores.replace("{time}", expiry.getExpiryDate(playerData.time, true), ignoreCase = true)
                                itemMeta.lore = listOf(*lores.split(", ").toTypedArray())
                            }
                            inventory.contents[index].itemMeta = itemMeta
                        }
                    }
                }
            }
        }
    }
}