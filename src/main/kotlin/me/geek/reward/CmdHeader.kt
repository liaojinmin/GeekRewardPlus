package me.geek.reward

import me.geek.reward.menu.impl.openMoneyUI
import me.geek.reward.api.DataManager
import me.geek.reward.api.DataManager.getBasicData
import me.geek.reward.api.DataManager.updateByTask
import org.bukkit.Bukkit

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
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


    @CommandBody(permission = "GeekRewardPlus.Command.reload")
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.sendMessage("[GeekRewardPlus] 已重新加载配置...")
        }
    }

    @CommandBody(permission = "GeekRewardPlus.Command.reset")
    val reset = subCommand {
        dynamic("玩家名称") {
            suggestion<CommandSender> { _, _ ->
                DataManager.getAllData().map { it.name }
            }
            execute<CommandSender> { _, context, _ ->
                Bukkit.getPlayer(context["玩家名称"])?.let {
                    it.getBasicData()?.let { data ->
                        data.reset()
                        data.updateByTask()
                    }
                }
            }
        }
    }

    @CommandBody(permission = "GeekRewardPlus.Command.data")
    val data = subCommand {
        // pms player points/money/time add/take vault
        dynamic("玩家名称") {
            suggestion<CommandSender> { _, _ ->
                Bukkit.getOnlinePlayers().map { it.name }
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

                            Bukkit.getPlayer(context["玩家名称"])?.let {
                                val data = it.getBasicData() ?: error("")
                                val action = context["操作类型"]
                                val value = context["值"]
                                when (context["对象类型"]) {
                                    "money" -> {
                                        if (action == "add") {
                                            data.money += value.toDouble()
                                            console().sendLang("Data-Add-value", it.name, "添加", value, "累计金币")
                                            sender.sendLang("Data-Add-value", it.name, "添加", value, "累计金币")
                                        }
                                        else {
                                            data.money -= value.toDouble()
                                            console().sendLang("Data-Add-value", it.name, "扣除", value, "累计金币")
                                            sender.sendLang("Data-Add-value", it.name, "扣除", value, "累计金币")
                                        }
                                    }
                                    "points" -> {
                                        if (action == "add") {
                                            data.points += context.args()[4].toInt()
                                            console().sendLang("Data-Add-value", it.name, "添加", value, "累计水晶")
                                            sender.sendLang("Data-Add-value", it.name, "添加", value, "累计水晶")
                                        } else {
                                            data.points -= context.args()[4].toInt()
                                            console().sendLang("Data-Add-value", it.name, "扣除", value, "累计水晶")
                                            sender.sendLang("Data-Add-value", it.name, "扣除", value, "累计水晶")
                                        }
                                    }
                                    "time" -> {
                                        if (action == "add") {
                                            data.time += Expiry.setExpiryMillis(value, false)
                                            console().sendLang("Data-Add-value", it.name, "添加", value, "累计在线")
                                            sender.sendLang("Data-Add-value", it.name, "添加", value, "累计在线")
                                        } else {
                                            data.time -= Expiry.setExpiryMillis(value, false)
                                            console().sendLang("Data-Add-value", it.name, "扣除", value, "累计在线")
                                            sender.sendLang("Data-Add-value", it.name, "扣除", value, "累计在线")
                                        }
                                    }
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
                when (text["菜单名称"]) {
                    "points" -> TODO()
                    "money" -> sender.getBasicData()?.let { sender.openMoneyUI(it) }
                    "time" -> TODO()
                    else -> {
                        sender.sendLang("Open-Menu-Err", text["菜单名称"])
                    }
                }
            }
        }
    }

    @CommandBody
    val main = mainCommand {
        createHelper()
    }
}