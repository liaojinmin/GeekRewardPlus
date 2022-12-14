package me.geek.reward.menu

import me.geek.GeekRewardPlus.debug
import me.geek.GeekRewardPlus.instance
import me.geek.reward.configuration.ConfigManager
import me.geek.reward.kether.sub.KetherAPI.instantKether
import me.geek.reward.menu.sub.Msession
import me.geek.reward.menu.sub.Micon
import me.geek.reward.modules.PlayersData
import me.geek.reward.modules.ModulesManage
import me.geek.reward.modules.ModulesManage.getPlayerData
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
import java.util.*
import kotlin.system.measureTimeMillis

/**
 * 作者: 老廖
 * 时间: 2022/8/19
 */
class Points(
    private val player: Player,
    private val session: Msession,
    private val inventory: Inventory
) {
    private val playerData: PlayersData = getPlayerData(player.uniqueId)!!
    private val iCon: List<Micon> = session.micon

    init {
        measureTimeMillis {
            build()
            actions()
        }.also {
            debug("&8加载菜单 &7${session.session} &8耗时 &7$it &8Ms") }
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
                            if (instantKether(player, icon.condition).any as Boolean && playerData.points >= icon.isValue.toInt()) {


                                debug("&8玩家 ${player.name} 尝试获取 ${icon.packID} 条件达成")
                                instantKether(player, icon.action.colorify()
                                    .replace("{player_name}", player.name)
                                    .replace("{player_uuid}", player.uniqueId.toString()))

                                Bukkit.getScheduler().scheduleSyncDelayedTask(instance) {
                                    val packID = icon.packID
                                    if (playerData.Points_key.isNotEmpty()) {
                                        val list: MutableList<String> = ArrayList(listOf(*playerData.Points_key))
                                            list.add(packID!!)
                                        val key  = list.toTypedArray()
                                        playerData.Points_key = key
                                        ModulesManage.update(playerData)
                                    } else {
                                        //   GeekRewardPro.say("else ");
                                        playerData.Points_key = arrayOf(packID!!)
                                        ModulesManage.update(playerData)
                                    }

                                }
                            } else {
                                debug("&8玩家 ${player.name} 尝试获取 ${icon.packID} 未达成条件")
                                instantKether(player, icon.deny.colorify())
                                return
                            }
                        }
                    }
                }
                /* else {
                    player.sendMessage(ConfigManager.gui_Click)
                }
                 */
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
        }, instance)
    }
    private fun build() {
        for ((index, value) in session.stringLayout.withIndex()) {
            if (value != ' ') {
                iCon.forEach { micon ->
                    if (micon.icon[0] == value) {
                        val itemMeta = inventory.contents[index].itemMeta
                        if (itemMeta != null) {
                            if (itemMeta.hasLore()) {
                                val list = mutableListOf<String>()
                                itemMeta.lore!!.forEach {
                                    when {
                                        it.contains("{state}") -> {
                                            list.add(it.replace("{state}", if (playerData.Points_key.contains(micon.packID)) { ConfigManager.OK } else if (playerData.points >= micon.isValue.toInt()) {
                                                ConfigManager.YES
                                            } else { ConfigManager.NO }))
                                        }
                                        it.contains("{points}") -> list.add(it.replace("{points}", playerData.points.toString()))
                                        it.contains("{相差}") -> {
                                            var amt = micon.isValue.toInt() - playerData.points
                                            if (amt < 0) { amt = 0 }
                                            list.add(it.replace("{相差}", amt.toString()))
                                        }
                                        else -> list.add(it)
                                    }
                                }

                                itemMeta.lore = list
                            }
                            inventory.contents[index].itemMeta = itemMeta
                        }
                    }
                }
            }
        }
    }
}