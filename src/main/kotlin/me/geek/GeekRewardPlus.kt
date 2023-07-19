package me.geek

import me.geek.reward.SetTings
import me.geek.reward.Placeholder
import me.geek.reward.api.RewardManager
import me.geek.reward.api.DataManager
import me.geek.reward.menu.Menu
import org.bukkit.Bukkit
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Platform
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin

/**
 * 作者: 老廖
 * 时间: 2022/8/18
 *
 **/
@RuntimeDependencies(
    RuntimeDependency(
        value = "!com.zaxxer:HikariCP:4.0.3",
        relocate = ["!com.zaxxer.hikari", "!com.zaxxer.hikari_4_0_3_rw"]),
)
object GeekRewardPlus : Plugin() {

    val instance by lazy { BukkitPlugin.getInstance() }

    const val version = "2.0"

    override fun onLoad() {
        Metrics(16328, version, Platform.BUKKIT)
        console().sendMessage("")
        console().sendMessage("正在加载 §3§lGeekRewardPlus  §f...  §8" + Bukkit.getVersion())
        console().sendMessage("")
    }

    override fun onEnable() {
        console().sendMessage("")
        console().sendMessage("  ________               __   __________                                .___")
        console().sendMessage(" /  _____/  ____   ____ |  | _\\______   \\ ______  _  _______ _______  __| _/")
        console().sendMessage("/   \\  ____/ __ \\_/ __ \\|  |/ /|       _// __ \\ \\/ \\/ /\\__  \\\\_  __ \\/ __ | ")
        console().sendMessage("\\    \\_\\  \\  ___/\\  ___/|    < |    |   \\  ___/\\     /  / __ \\|  | \\/ /_/ | ")
        console().sendMessage(" \\______  /\\___  >\\___  >__|_ \\|____|_  /\\___  >\\/\\_/  (____  /__|  \\____ | ")
        console().sendMessage("        \\/     \\/     \\/     \\/       \\/     \\/             \\/           \\/ ")
        console().sendMessage("")
        console().sendMessage("")
        console().sendMessage("     §eGeekRewardPlus §bv$version §7by §awww.geekcraft.ink")
        console().sendMessage("     §8适用于Bukkit: §71.12.x-1.20.x §8当前: §7" + Bukkit.getName())
        console().sendMessage("")

        // 配置文件加载
        SetTings.init()

        // 奖励配置加载
        RewardManager.load()

        // 数据库启动
        DataManager.start()

        // 菜单加载
        Menu.reload()

        // hook plugin
        hook()
    }

    override fun onDisable() {
        DataManager.saveAllData()
        DataManager.close()
    }


    private fun hook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            say("&7软依赖 &fPlaceholderAPI &7已兼容.")
            Placeholder().register()
        }
    }


    fun say(msg: String) {
        console().sendMessage("§8[§eGeek§6Reward§ePlus§8] ${msg.replace("&", "§")}")
    }


    fun debug(msg: String) {
        if (SetTings.deBug) {
            console().sendMessage("§8[§6GeekRewardPlus§8]§8[§cDeBug§8]§7 ${msg.replace("&", "§")}")
        }
    }
}