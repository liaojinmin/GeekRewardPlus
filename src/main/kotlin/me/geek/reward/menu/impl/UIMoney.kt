package me.geek.reward.menu.impl

import me.geek.reward.api.PlayerData
import me.geek.reward.kether.KetherAPI
import me.geek.reward.menu.Menu
import me.geek.reward.menu.MenuData
import me.geek.reward.menu.MenuIcon
import org.bukkit.entity.Player
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.util.buildItem

/**
 * @作者: 老廖
 * @时间: 2023/7/16 16:14
 * @包: me.geek.reward.menu.impl
 */
fun Player.openMoneyUI(data: PlayerData, menuData: MenuData = Menu.moneyMenuData) {

    openMenu<Linked<MenuIcon>>(menuData.title.replacePlaceholder(this)) {

        map(*menuData.layout)

        rows(menuData.layout.size)

        slots(menuData.itemUISlots)

        elements { menuData.menuIcon.values.filter { !it.packID.isNullOrEmpty() } }

        onGenerate { player, icon, _, _ ->
            buildItem(icon.mats) {
                val state = icon.parse(data.money, data.moneyKey)
                name = icon.name.replacePlaceholder(player)
                    .replace("{money}", data.money.toString())
                    .replace("{value}", icon.isValue)
                    .replace("{state}", state)
                icon.lore.forEach {
                    lore.add(it.replacePlaceholder(player)
                        .replace("{money}", data.money.toString())
                        .replace("{value}", icon.isValue)
                        .replace("{state}", state))
                }
                customModelData = icon.model
            }
        }


        onClick { _, icon ->
            if (icon.packID == null) return@onClick
            if (data.money >= icon.isValue.toDouble()) {
                if (data.moneyKey.find { it == icon.packID } != null) {
                    KetherAPI.instantKether(this@openMoneyUI, icon.achieve)
                } else {
                    // 允许领取
                    data.moneyKey.add(icon.packID)
                    KetherAPI.instantKether(this@openMoneyUI, icon.allow)
                }
            } else KetherAPI.instantKether(this@openMoneyUI, icon.deny)
        }

        // 构建其它图标
        menuData.menuIcon.forEach { (key, icon) ->
            when (key) {
                '<' -> {
                    set(icon.char, buildItem(icon.mats) {
                        name = icon.name.replacePlaceholder(this@openMoneyUI)
                        lore.addAll(icon.lore.replacePlaceholder(this@openMoneyUI))
                        customModelData = icon.model
                        hideAll()
                    }) {
                        if (icon.action.isNotEmpty()) KetherAPI.eval(this.clicker, icon.action)
                        if (hasPreviousPage()) {
                            page(page-1)
                            openInventory(build())
                        }
                    }
                }
                '>' -> {
                    set(icon.char, buildItem(icon.mats) {
                        name = icon.name.replacePlaceholder(this@openMoneyUI)
                        lore.addAll(icon.lore.replacePlaceholder(this@openMoneyUI))
                        customModelData = icon.model
                        hideAll()
                    }) {
                        if (icon.action.isNotEmpty()) KetherAPI.eval(this.clicker, icon.action)
                        if (hasPreviousPage()) {
                            page(page+1)
                            openInventory(build())
                        }
                    }
                }
                else -> {
                    if (icon.packID.isNullOrEmpty()) {
                        set(key, buildItem(icon.mats) {
                            name = icon.name.replacePlaceholder(this@openMoneyUI)
                            lore.addAll(icon.lore.replacePlaceholder(this@openMoneyUI))
                            customModelData = icon.model
                        }) {
                            if (icon.action.isNotEmpty()) KetherAPI.eval(this.clicker, icon.action)
                        }
                    }
                }
            }
        }
    }
}