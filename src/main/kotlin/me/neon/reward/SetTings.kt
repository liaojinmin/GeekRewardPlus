package me.neon.reward

import me.neon.reward.service.ConfigSql
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.module.chat.colored
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.Configuration.Companion.getObject
import taboolib.platform.BukkitPlugin
import kotlin.system.measureTimeMillis

/**
 * 作者: 老廖
 * 时间: 2023/4/26
 *
 **/
@PlatformSide([Platform.BUKKIT])
object SetTings {

    @Config(value = "settings.yml", autoReload = true)
    lateinit var config: ConfigFile
        private set

    fun init() {
        onLoadSetTings()
        config.onReload { onLoadSetTings() }
    }

    lateinit var configSql: ConfigSql

    lateinit var setConfig: SetConfig

    var deBug: Boolean = false

    var checkUpdate: Boolean = true



    private fun onLoadSetTings() {
        measureTimeMillis {
            deBug = config.getBoolean("debug", false)
            checkUpdate = config.getBoolean("checkUpdate", true)
            configSql = config.getObject("data_storage", false)
            setConfig = config.getObject("set", false)
            try {
                State.Received.dis = config.getString("状态信息.已领取", "§a已领取")!!.colored()
                State.Available.dis = config.getString("状态信息.可领取", "§a可领取")!!.colored()
                State.NotAvailable.dis = config.getString("状态信息.不可领取", "§c不可领取")!!.colored()
            } catch (ex: Exception) {
                NeonRewardPlus.say("你的配置文件缺失，请前往WIKI查阅最新配置文件...")
            }
            configSql.sqlite = BukkitPlugin.getInstance().dataFolder
        }
    }
    data class SetConfig(
        val closePoints: Boolean = false,
        val closeMoney: Boolean = false,
        val closeTime: Boolean = false,
        val timeReset: Boolean = false,
        val boardTime: Int = 600
    )

    enum class State(var dis: String) {
        Received("§a已领取"),

        Available("§a可领取"),

        NotAvailable("§c不可领取")
    }



}