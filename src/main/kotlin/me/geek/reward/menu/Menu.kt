package me.geek.reward.menu

import me.geek.GeekRewardPlus
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.common.platform.function.releaseResourceFile
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.XMaterial
import taboolib.module.chat.colored
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.SecuredFile
import taboolib.module.configuration.util.getMap
import taboolib.module.lang.sendLang
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis

/**
 * 作者: 老廖
 * 时间: 2023/7/16
 */

object Menu {


    private val AIR = ItemStack(Material.AIR)

    @Config(value = "money.yml", autoReload = true)
    lateinit var money: ConfigFile
        private set
    lateinit var moneyMenuData: MenuData
        private set

    private fun loadMoney() {
        measureTimeMillis {
            val title = money.getString("title")!!.colored()
            val layouts = money.getStringList("layout")
            val icon = mutableMapOf<Char, MenuIcon>()
            money.getConfigurationSection("icons")!!.getKeys(false).forEach {
                val key = "icons.$it"
                icon[it.first()] = MenuIcon(
                    it.first(),
                    XMaterial.valueOf(money.getString("$key.display.material") ?: "PAPER"),
                    money.getInt("$key.display.model"),
                    money.getString("$key.display.name")?.colored() ?: "",
                    money.getStringList("$key.display.lore").colored(),
                    money.getString("$key.display.packID"),
                    money.getString("$key.display.isValue") ?: "0",
                    money.getStringList("$key.Require.achieve").joinToString("\n").colored(),
                    money.getStringList("$key.Require.deny").joinToString("\n").colored(),
                    money.getStringList("$key.Require.allow").joinToString("\n").colored(),
                    money.getStringList("$key.action").joinToString("\n").colored()
                )
            }
            moneyMenuData = MenuData(title, layouts.toTypedArray(), icon)
        }.also { GeekRewardPlus.say("§7加载 §e金币累计奖励 §7配置... §8(耗时 $it ms)") }
    }


}