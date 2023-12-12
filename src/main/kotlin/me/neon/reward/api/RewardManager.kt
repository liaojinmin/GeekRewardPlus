package me.neon.reward.api

import me.neon.reward.NeonRewardPlus
import me.neon.reward.SetTings
import me.neon.reward.api.DataManager.getBasicData
import me.neon.reward.api.data.ExpIryBuilder
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.releaseResourceFile
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor
import taboolib.common5.FileWatcher
import taboolib.module.chat.colored
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.SecuredFile
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.sendLang
import java.io.File
import java.util.*
import kotlin.system.measureTimeMillis

/**
 * @作者: 老廖
 * @时间: 2023/7/19 20:25
 * @包: me.geek.reward.api
 */
object RewardManager {
    /**
     * 计时线程
     */
    private var refreshTimeTask: PlatformExecutor.PlatformTask? = null

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
                NeonRewardPlus.say("§7加载 §f$index §7奖励配置... §8(耗时 $it ms)")
            }
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun start() {
        refreshTimeTask?.cancel()
        refreshTimeTask = submitAsync(delay = 20, period = 20) {
            try {
                val player = Bukkit.getOnlinePlayers().toList().listIterator()
                while (player.hasNext()) {
                    val p = player.next()
                    if (p.isOnline) {
                        val data = p.getBasicData()
                        if (data != null) {
                            if (SetTings.setConfig.timeReset) {
                                // 重置在线时间
                                if (data.timeDay <= System.currentTimeMillis()) {
                                    p.sendLang("time-reset")
                                    data.timeDay = getTodayStartTime()
                                    data.timeKey.clear()
                                    data.time = ExpIryBuilder("0", false)
                                }
                            }
                            data.time.autoUpdate()
                        }
                    }
                }
            } catch (ex: Exception) {
                NeonRewardPlus.say("计时线程出现异常")
                ex.printStackTrace()
            }
        }
    }
    fun getTodayStartTime(): Long {
        //设置时区
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))
        calendar[Calendar.HOUR_OF_DAY] = 24
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        return calendar.timeInMillis
    }

    private fun loadFile(index: String, out: ConfigFile, isReload: Boolean = false) {
        out.getConfigurationSection(index)!!.getKeys(false).forEach { id ->
            val key = "$index.$id"
            val priority = out.getInt("$key.priority")
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
                    pointsConfigCache.add(RewardConfig(id, priority, value.toInt(), info, require))
                    pointsConfigCache.sortBy { it.priority }
                }
                "money" -> {
                    if (isReload) {
                        moneyConfigCache.removeIf { it.id == id }
                    }
                    moneyConfigCache.add(RewardConfig(id, priority, value.toInt(), info, require))
                    moneyConfigCache.sortBy { it.priority }
                }
                "time" -> {
                    if (isReload) {
                        timeConfigCache.removeIf { it.id == id }
                    }
                    timeConfigCache.add(RewardConfig(id, priority, ExpIryBuilder(value, false), info, require))
                    timeConfigCache.sortBy { it.priority }
                }
            }
        }
        if (isReload)  NeonRewardPlus.say("§7自动重载 §f${out.file!!.name} §7配置...")
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