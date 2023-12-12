package me.neon.reward

import me.neon.reward.api.RewardManager
import me.neon.reward.api.DataManager
import me.neon.reward.menu.Menu
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submitAsync
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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
object NeonRewardPlus : Plugin() {

    val instance by lazy { BukkitPlugin.getInstance() }

    private val url = URL("https://raw.githubusercontent.com/liaojinmin/GeekRewardPlus/GeekRewardPlus-V2/version.txt")

    const val version = "2.3"

    override fun onLoad() {
        Metrics(16328, version, Platform.BUKKIT)
        console().sendMessage("")
        console().sendMessage("正在加载 §3§lNeonRewardPlus  §f...  §8" + Bukkit.getVersion())
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
        console().sendMessage("     §eNeonRewardPlus §bv$version §7by §awww.hsdush.com")
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
        console().sendMessage("§8[§bNeonRewardPlus§8] ${msg.replace("&", "§")}")
    }


    fun debug(msg: String) {
        if (SetTings.deBug) {
            console().sendMessage("§8[§eNeonRewardPlus§8]§8[§cDeBug§8]§7 ${msg.replace("&", "§")}")
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun checkUp() {
        if (SetTings.checkUpdate) {
            submitAsync {
                say("&bUPDATE &8| &3正在检查版本更新...")
                val now = version.filter { it.isDigit() }.toInt()
                val new = getVersion()?.filter { it.isDigit() }?.toInt() ?: now
                debug("&bUPDATE &8| &c$now vs &a$new")
                if (new > now) {
                    say("&bUPDATE &8| &3有新版本可用: &a$new &3当前版本: $now, 通过下方链接下载!")
                    say("&f https://www.mcbbs.net/thread-1380923-1-1.html")
                } else {
                    say("&bUPDATE &8| &3当前已是最新版本...")
                }
            }
        }
    }

    private fun getVersion(): String? {
        var connection: HttpURLConnection? = null
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            return BufferedReader(InputStreamReader(connection.inputStream)).readLine()
        } catch (ignored: Throwable) {
            say("&bUPDATE &8| &e网络异常，无法获取更新信息...")
        } finally {
            connection?.disconnect()
        }
        return null
    }
}