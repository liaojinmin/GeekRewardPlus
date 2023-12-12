package me.neon.reward.menu.impl

import me.neon.reward.NeonRewardPlus
import me.neon.reward.api.RewardConfig
import me.neon.reward.api.RewardManager
import me.neon.reward.api.data.ExpIryBuilder
import me.neon.reward.api.data.PlayerData
import me.neon.reward.kether.KetherAPI
import me.neon.reward.menu.Menu
import me.neon.reward.menu.MenuData
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
fun Player.openTimeUI(data: PlayerData, menuData: MenuData = Menu.timeMenuData) {

    openMenu<Linked<RewardConfig<ExpIryBuilder>>>(menuData.title.replacePlaceholder(this)) {

        map(*menuData.layout)

        rows(menuData.layout.size)

        slots(menuData.itemUISlots)

        elements { RewardManager.timeConfigCache }

        onGenerate { player, e, _, _ ->
            menuData.menuIcon['@']?.let { icon ->
                buildItem(icon.mats) {

                    val state = e.parse(data.timeKey, data.time)

                    val max = e.parseValue()

                    // 解析展示物品名称
                    name = icon.name.replacePlaceholder(player)
                        .replace("{max_value}", max)
                        .replace("{now_value}", data.time.getExpiryFormat())
                        .replace("{state}", state)

                    // 添加图标描述
                    icon.lore.forEach {
                        if (it.contains("{info}")) {
                            e.info.forEach { info ->
                                lore.add(info.replacePlaceholder(player))
                            }
                        } else {
                            lore.add(
                                it.replacePlaceholder(player)
                                    .replace("{max_value}", max)
                                    .replace("{now_value}", data.time.getExpiryFormat())
                                    .replace("{state}", state)
                            )
                        }
                    }
                    customModelData = icon.model
                }
            } ?: error("找不到可用的奖励展示图标配置...")
        }


        onClick { event, element ->
            val player = event.clicker
            NeonRewardPlus.debug("time ac: ${data.time.millis} >= ${element.value.millis}")
            if (data.time.millis >= element.value.millis) {
                if (data.timeKey.find { it == element.id } != null) {
                    KetherAPI.instantKether(this@openTimeUI, element.require.achieve.replacePlaceholder(player))
                } else {
                    // 允许领取
                    data.timeKey.add(element.id)
                    KetherAPI.instantKether(this@openTimeUI, element.require.allow.replacePlaceholder(player))
                }
            } else KetherAPI.instantKether(this@openTimeUI, element.require.deny.replacePlaceholder(player))
        }



        // 构建其它图标
        menuData.menuIcon.forEach { (key, icon) ->
            when (key) {
                '<' -> {
                    set(icon.char, buildItem(icon.mats) {
                        name = icon.name.replacePlaceholder(this@openTimeUI)
                        lore.addAll(icon.lore.replacePlaceholder(this@openTimeUI))
                        customModelData = icon.model
                        hideAll()
                    }) {
                        if (icon.action.isNotEmpty()) KetherAPI.eval(this.clicker, icon.action.replacePlaceholder(this.clicker))
                        if (hasPreviousPage()) {
                            page(page-1)
                            openInventory(build())
                        }
                    }
                }
                '>' -> {
                    set(icon.char, buildItem(icon.mats) {
                        name = icon.name.replacePlaceholder(this@openTimeUI)
                        lore.addAll(icon.lore.replacePlaceholder(this@openTimeUI))
                        customModelData = icon.model
                        hideAll()
                    }) {
                        if (icon.action.isNotEmpty()) KetherAPI.eval(this.clicker, icon.action.replacePlaceholder(this.clicker))
                        if (hasPreviousPage()) {
                            page(page+1)
                            openInventory(build())
                        }
                    }
                }
                // 其它任意图标如果有动作则执行
                else -> {
                    if (key != '@')
                    set(key, buildItem(icon.mats) {
                        name = icon.name.replacePlaceholder(this@openTimeUI)
                        lore.addAll(icon.lore.replacePlaceholder(this@openTimeUI))
                        customModelData = icon.model
                    }) {
                        if (icon.action.isNotEmpty()) KetherAPI.eval(this.clicker, icon.action.replacePlaceholder(this.clicker))
                    }
                }
            }
        }
    }
}