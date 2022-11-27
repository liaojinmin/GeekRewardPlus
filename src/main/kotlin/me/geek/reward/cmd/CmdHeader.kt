package me.geek.reward.cmd

import com.google.common.base.Joiner
import me.geek.GeekRewardPlus
import me.geek.GeekRewardPlus.say
import me.geek.reward.configuration.RewardFiles
import me.geek.reward.database.migrator
import me.geek.reward.database.migratorYml
import me.geek.reward.kether.sub.KetherAPI
import me.geek.reward.menu.Menu
import me.geek.reward.menu.Money
import me.geek.reward.menu.Points
import me.geek.reward.menu.Time
import me.geek.reward.modules.ModulesManage
import me.geek.reward.modules.ModulesManage.expiry
import org.bukkit.Bukkit

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console

import taboolib.expansion.createHelper
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang


/**
 * 作者: 老廖
 * 时间: 2022/8/18
 *
 **/
@CommandHeader(name = "GeekRw", aliases = ["rw","pms"], permissionDefault = PermissionDefault.TRUE )
object CmdHeader {

    @CommandBody(permission = "GeekRewardPlus.admin")
    val migrator = subCommand {
        // geekRw migrator <>
        dynamic("目标数据库") {
            suggestion<CommandSender>{ _, _ ->
                listOf("目标数据库","目标文件夹")
            }
            execute<CommandSender> { _, context, _ ->
                val data = context.args()[1].split(" ")
                if (data[0] == "目标数据库") {
                    migrator(data[1])
                } else {
                    say(data[1])
                    migratorYml(data[1])
                }
            }
        }
    }

    @CommandBody(permission = "GeekRewardPlus.admin")
    val reload = subCommand {
        execute<CommandSender> { _, _, _ ->
            RewardFiles.reloadAll()
        }
    }

    @CommandBody(permission = "GeekRewardPlus.admin")
    val reset = subCommand {
        dynamic("玩家名称") {
            suggestion<CommandSender> { _, _ ->
                ModulesManage.getPlayersDataMap().values.map { it.name }
            }
            execute<CommandSender> { _, context, _ ->
                Bukkit.getOfflinePlayer(context.args()[1]).let {
                    ModulesManage.getPlayerData(it.uniqueId).let { data ->
                        data.reset()
                        ModulesManage.update(data)
                    }
                }
            }
        }
    }
    @CommandBody(permission = "GeekRewardPlus.admin")
    val data = subCommand {
        // pms player points/money/time add/take vault
        dynamic("玩家名称") {
            suggestion<CommandSender> { _, _ ->
                ModulesManage.getPlayersDataMap().values.map { it.name }
            }
            dynamic("对象类型") {
                suggestion<CommandSender> { _, _ ->
                    listOf("points","money","time")
                }
                dynamic("操作类型") {
                    suggestion<CommandSender> { _, _ ->
                        listOf("add","take")
                    }
                    dynamic("值") {
                        execute<CommandSender> { sender, context, _ ->
                            for (out in context.args()) {
                                GeekRewardPlus.debug(out)
                            }
                            val player = Bukkit.getOfflinePlayer(context.args()[1])
                            val data = ModulesManage.getPlayerData(player.uniqueId)
                            when (context.args()[2]) {
                                "money" -> {
                                    if (context.args()[3] == "add") {
                                        data.money += context.args()[4].toDouble()
                                        sender.sendLang("Data-Add-value", context.args()[1], "添加", context.args()[4], "累计金币")
                                    }
                                    else {
                                        data.money -= context.args()[4].toDouble()
                                        sender.sendLang("Data-Add-value", context.args()[1], "扣除", context.args()[4], "累计金币")
                                    }
                                }
                                "points" -> if (context.args()[3] == "add") {
                                    data.points += context.args()[4].toInt()
                                    sender.sendLang("Data-Add-value", context.args()[1], "添加", context.args()[4], "累计点券")
                                } else {
                                    data.points -= context.args()[4].toInt()
                                    sender.sendLang("Data-Add-value", context.args()[1], "扣除", context.args()[4], "累计金币")
                                }
                                "time" -> if (context.args()[3] == "add") {
                                    data.time += expiry.setExpiryMillis(context.args()[4], false)
                                    sender.sendLang("Data-Add-value", context.args()[1], "添加", context.args()[4], "累计在线")
                                } else {
                                    data.time -= expiry.setExpiryMillis(context.args()[4], false)
                                    sender.sendLang("Data-Add-value", context.args()[1], "扣除", context.args()[4], "累计在线")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    val open = subCommand {
        dynamic("菜单名称") {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                listOf("points", "money","time")
            }
            execute<Player> { sender, text, _ ->
                val value = text.args()
                value[1].let {
                    when (it) {
                        "points" -> Points(sender, Menu.getSession(it), Menu.BuildInventory(sender, it))
                        "money" -> Money(sender, Menu.getSession(it), Menu.BuildInventory(sender, it))
                        "time" -> Time(sender, Menu.getSession(it), Menu.BuildInventory(sender, it))
                        else -> {
                            adaptPlayer(sender).sendLang("Open-Menu-Err", it)
                        }
                    }
                }
            }
        }
    }
    @CommandBody(permission = "GeekRewardPlus.test")
    val test = subCommand {
        execute { sender, text, _ ->
            val value = Joiner.on(",").join(text.args()).replace("test ", "").split(",")
            say("${value[0]} 返回值: ${KetherAPI.instantKether(sender, value[0])}")
        }
    }

    @CommandBody
    val main = mainCommand {
        createHelper()
    }
}