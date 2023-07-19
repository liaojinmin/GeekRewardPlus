package me.geek.reward.api

import me.geek.GeekRewardPlus
import me.geek.reward.api.data.ExpIryBuilder
import org.bukkit.Material
import taboolib.common.platform.function.releaseResourceFile
import taboolib.common5.FileWatcher
import taboolib.module.chat.colored
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.Configuration.Companion.getObject
import taboolib.module.configuration.SecuredFile
import taboolib.platform.BukkitPlugin
import java.io.File
import kotlin.system.measureTimeMillis

/**
 * @作者: 老廖
 * @时间: 2023/7/19 20:25
 * @包: me.geek.reward.api
 */
object RewardManager {

    val pointsConfigCache: MutableList<RewardConfig<Int>> = mutableListOf()

    val moneyConfigCache: MutableList<RewardConfig<Int>> = mutableListOf()

    val timeConfigCache: MutableList<RewardConfig<ExpIryBuilder>> = mutableListOf()

    fun load() {
        pointsConfigCache.clear()
        moneyConfigCache.clear()
        timeConfigCache.clear()
        listOf("points", "money", "time").forEach { index ->

            fileLoad(saveDefault(index), {
                loadFile(index, it)
                it.onReload {
                    loadFile(index, it, true)
                }
            }) {
                GeekRewardPlus.say("§7加载 §f$index §7奖励配置... §8(耗时 $it ms)")
            }
        }
    }

    private fun loadFile(index: String, out: ConfigFile, isReload: Boolean = false) {
        out.getConfigurationSection(index)!!.getKeys(false).forEach { id ->
            val key = "$index.$id"
            val value = out.getString("$key.value") ?: "0"
            val info = out.getStringList("$key.info").colored()
            val achieve = out.getStringList("$key.Require.achieve").joinToString("\n").colored()
            val deny = out.getStringList("$key.Require.deny").joinToString("\n").colored()
            val allow = out.getStringList("$key.Require.allow").joinToString("\n").colored()
            val require = RewardRequire(achieve, deny, allow)
            when (index) {
                "points" -> {
                    if (isReload) {
                        pointsConfigCache.removeIf { it.id == id }
                    }
                    pointsConfigCache.add(RewardConfig(id, value.toInt(), info, require))
                }
                "money" -> {
                    if (isReload) {
                        moneyConfigCache.removeIf { it.id == id }
                    }
                    moneyConfigCache.add(RewardConfig(id, value.toInt(), info, require))
                }
                "time" -> {
                    if (isReload) {
                        timeConfigCache.removeIf { it.id == id }
                    }
                    timeConfigCache.add(RewardConfig(id, ExpIryBuilder(value), info, require))
                }
            }
        }
        if (isReload)  GeekRewardPlus.say("§7自动重载 §f${out.file!!.name} §7配置...")
    }

    private fun fileLoad(f: File, func: (sf: SecuredFile) -> Unit, end: (ms: Long) -> Unit = {}) {
        val list = mutableListOf<File>()
        measureTimeMillis {
            list.also {
                it.addAll(forFile(f))
            }
            list.forEach { file ->
                val out = SecuredFile.loadConfiguration(file)
                // 添加自动重载事件
                FileWatcher.INSTANCE.addSimpleListener(file) {
                    out.loadFromFile(file)
                }
                func(out)
            }
        }.also { end(it) }
    }
    private fun forFile(file: File): List<File> {
        return mutableListOf<File>().run {
            if (file.isDirectory) {
                file.listFiles()?.forEach {
                    addAll(forFile(it))
                }
            } else if (file.exists() && file.absolutePath.endsWith(".yml")) {
                add(file)
            }
            this
        }
    }

    private fun saveDefault(name: String): File {
        val dir = File(BukkitPlugin.getInstance().dataFolder, name)
        if (!dir.exists()) {
            arrayOf(
                "$name/def.yml",
            ).forEach { releaseResourceFile(it, true) }
        }
        return dir
    }
}