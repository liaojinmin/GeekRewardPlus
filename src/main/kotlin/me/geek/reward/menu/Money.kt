package me.geek.reward.menu

import me.geek.GeekRewardPlus
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
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import taboolib.platform.compat.replacePlaceholder
import java.util.*
import kotlin.system.measureTimeMillis

/**
 * 作者: 老廖
 * 时间: 2022/8/19
 */
class Money(
    private val player: Player,
    private val msession: Msession,
    private val inventory: Inventory
    ) {
    private val plugin: Plugin = instance
    private val playerData: PlayersData = getPlayerData(player.uniqueId)!!
    private val iCon: List<Micon> = msession.micon


    init {
        measureTimeMillis {
            build()
            actions()
        }.also {
            GeekRewardPlus.debug("&8加载菜单 &7${msession.session} &8耗时 &7$it &8Ms")
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
                    if (event.rawSlot > msession.stringLayout.length) return
                    val id = msession.stringLayout[event.rawSlot].toString()
                    for (icon in iCon) {
                        if (icon.icon == id && icon.packID != null) {
                            if (instantKether(player, icon.condition).any as Boolean && playerData.money >= icon.isValue.toDouble()) {
                                GeekRewardPlus.debug("&8玩家 ${player.name} 尝试获取 ${icon.packID} 条件达成")
                                instantKether(player, icon.action.colorify()
                                    .replace("{player_name}", player.name)
                                    .replace("{player_uuid}", player.uniqueId.toString()))
                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin) {
                                    val packID = icon.packID
                                    if (playerData.Money_key.isNotEmpty()) {
                                        val list: MutableList<String> = ArrayList(listOf(*playerData.Money_key))
                                            list.add(packID!!)
                                        val key  = list.toTypedArray()
                                        playerData.Money_key = key
                                        ModulesManage.update(playerData)
                                    } else {
                                        //   GeekRewardPro.say("else ");
                                        playerData.Money_key = arrayOf(packID!!)
                                        ModulesManage.update(playerData)
                                    }

                                }
                            } else {
                                GeekRewardPlus.debug("&8玩家 ${player.name} 尝试获取 ${icon.packID} 未达成条件")
                                instantKether(player, icon.deny.colorify())
                                return
                            }
                        }
                    }
                }
                /*else {
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
        }, plugin)
    }
    private fun build() {
        for ((index, value) in msession.stringLayout.withIndex()) {
            if (value != ' ') {
                iCon.forEach { micon ->
                    if (micon.icon[0] == value) {
                        val itemMeta = inventory.contents[index].itemMeta
                        if (itemMeta != null) {
                            if (itemMeta.hasLore()) {
                                var lores = itemMeta.lore!!.joinToString().replacePlaceholder(player)

                                lores = if (playerData.Money_key.contains(micon.packID)) {
                                    lores.replace("{state}", ConfigManager.OK, ignoreCase = true)
                                } else if (playerData.money >= micon.isValue.toDouble()) {
                                    lores.replace("{state}", ConfigManager.YES, ignoreCase = true)
                                } else {
                                    lores.replace("{state}", ConfigManager.NO, ignoreCase = true)
                                }
                                lores = lores.replace("{money}", playerData.money.toString(), ignoreCase = true)
                                itemMeta.lore = lores.split(", ")
                            }
                            inventory.contents[index].itemMeta = itemMeta
                        }
                    }
                }
            }
        }
    }
}