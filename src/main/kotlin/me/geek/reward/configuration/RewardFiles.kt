package me.geek.reward.configuration

import me.geek.reward.configuration.ConfigManager.loadConfig
import me.geek.reward.menu.Menu
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

/**
 * 作者: 老廖
 * 时间: 2022/8/27
 *
 **/
@PlatformSide([Platform.BUKKIT])
object RewardFiles {


    @Config("config.yml", autoReload = true)
    lateinit var config: ConfigFile
        private set


    @Awake(LifeCycle.ACTIVE)
    fun init() {
        config.onReload { loadConfig() }
    }

    fun reloadAll() {
        Menu.CloseGui()
        config.reload()
        Menu.loadMenu()

    }

    private fun saveAll() {
        config.saveToFile()
    }
}