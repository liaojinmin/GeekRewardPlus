package me.geek

import me.geek.reward.configuration.ConfigManager
import me.geek.reward.database.DataManage
import me.geek.reward.menu.Menu
import me.geek.reward.modules.Metrics.Metrics
import me.geek.reward.modules.ModulesManage
import me.geek.reward.modules.task.OnlineTime
import me.geek.reward.modules.task.LocalTop
import me.geek.reward.utils.HexUtils.colorify
import org.bukkit.Bukkit
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
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
// MinecraftVersion.majorLegacy
object GeekRewardPlus : Plugin() {
    val instance by lazy { BukkitPlugin.getInstance() }
    val top by lazy { LocalTop() }
    const val version = "1.1"
    val BukkitVersion = Bukkit.getVersion().substringAfter("MC:").filter { it.isDigit() }.toInt()
    override fun onLoad() {
        Metrics(instance, 16328)
        title("")
        title("正在加载 §3§lGeekRewardPlus  §f...  §8" + Bukkit.getVersion())
        title("")
    }

    override fun onEnable() {
        title("")
        title("  ________               __   __________                                .___")
        title(" /  _____/  ____   ____ |  | _\\______   \\ ______  _  _______ _______  __| _/")
        title("/   \\  ____/ __ \\_/ __ \\|  |/ /|       _// __ \\ \\/ \\/ /\\__  \\\\_  __ \\/ __ | ")
        title("\\    \\_\\  \\  ___/\\  ___/|    < |    |   \\  ___/\\     /  / __ \\|  | \\/ /_/ | ")
        title(" \\______  /\\___  >\\___  >__|_ \\|____|_  /\\___  >\\/\\_/  (____  /__|  \\____ | ")
        title("        \\/     \\/     \\/     \\/       \\/     \\/             \\/           \\/ ")
        title("")
        title("")
        title("     §aGeekRewardPlus §bv$version §7by §awww.geekcraft.ink")
        title("     §8适用于Bukkit: §71.12.2-1.19.2 §8当前: §7" + Bukkit.getName() + BukkitVersion)
        title("")
        // 配置文件加载
        ConfigManager.loadConfig()
        // 数据库启动
        DataManage.start()

        // 模块管理初始化
        ModulesManage.onStart()
        // 获取所有玩家数据
        ModulesManage.getPlayerData()
        //开始计算在线时间
        OnlineTime().calculate()
        // 唤起排行榜任务
        top.pointsTop()
       // LocalTop().pointsTop()
    }

    override fun onDisable() {
        OnlineTime().saveData()
        Menu.CloseGui()
        DataManage.close()
    }

    @JvmStatic
    fun say(msg: String) {
        if (BukkitVersion >= 1160)
            console().sendMessage(colorify("&8[<g#2:#FF00FF:#FFFAFA>GeekRewardPlus&8] &7$msg"))
        else
            console().sendMessage("§8[§6GeekReward§ePlus§8] ${msg.replace("&", "§")}")
    }

    private fun title(msg: String) {
        console().sendMessage(msg)
    }

    @JvmStatic
    fun debug(msg: String) {
        if(ConfigManager.DeBug) {
            if (BukkitVersion >= 1160)
                console().sendMessage(colorify("&8[<g#2:#FF00FF:#FFFAFA>GeekRewardPlus&8] &cDeBug &8| &7$msg"))
            else
                console().sendMessage("§8[§6GeekReward§ePlus§8] ${msg.replace("&", "§")}")
        }
    }
}