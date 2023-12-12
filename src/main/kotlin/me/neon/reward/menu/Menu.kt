package me.neon.reward.menu

import me.neon.reward.NeonRewardPlus
import taboolib.library.xseries.XMaterial
import taboolib.module.chat.colored
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import kotlin.system.measureTimeMillis

/**
 * 作者: 老廖
 * 时间: 2023/7/16
 */

object Menu {

    @Config(value = "menu.yml", autoReload = true)
    lateinit var menu: ConfigFile
        private set

    lateinit var moneyMenuData: MenuData
        private set

    lateinit var pointsMenuData: MenuData
        private set

    lateinit var timeMenuData: MenuData
        private set

    fun reload() {
        load()
        menu.onReload { load() }
    }

    private fun load() {
        measureTimeMillis {
            listOf("points","money","time").forEach { id ->

                val title = menu.getString("$id.title")!!.colored()
                val layouts = menu.getStringList("$id.layout")
                val icon = mutableMapOf<Char, MenuIcon>()
                menu.getConfigurationSection("$id.icons")!!.getKeys(false).forEach {
                    val key = "$id.icons.$it"
                    icon[it.first()] = MenuIcon(
                        it.first(),
                        XMaterial.valueOf(menu.getString("$key.display.mats") ?: "PAPER"),
                        menu.getInt("$key.display.model"),
                        menu.getString("$key.display.name")?.colored() ?: "",
                        menu.getStringList("$key.display.lore").colored(),
                        menu.getStringList("$key.action").joinToString("\n").colored()
                    )
                }

                when (id) {
                    "points" -> pointsMenuData = MenuData(title, layouts.toTypedArray(), icon)
                    "money" -> moneyMenuData = MenuData(title, layouts.toTypedArray(), icon)
                    "time" -> timeMenuData = MenuData(title, layouts.toTypedArray(), icon)
                }
            }
        }.also { NeonRewardPlus.say("§7加载奖励菜单配置... §8(耗时 $it ms)") }
    }


}