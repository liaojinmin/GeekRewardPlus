package me.neon.reward

import me.neon.reward.api.DataManager
import me.neon.reward.api.DataManager.getBasicData
import me.neon.reward.api.DataManager.updateByTask
import me.neon.reward.api.RewardManager
import me.neon.reward.api.data.ExpIryBuilder
import me.neon.reward.menu.Menu
import me.neon.reward.menu.impl.openMoneyUI
import me.neon.reward.menu.impl.openPointsUI
import me.neon.reward.menu.impl.openTimeUI
import org.bukkit.Bukkit

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.common.platform.function.console

import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang


/**
 * 作者: 老廖
 * 时间: 2022/8/18
 *
 **/
@CommandHeader(name = "NeonRw", aliases = ["rw","pms"], permissionDefault = PermissionDefault.TRUE )
object CmdHeader {


    @CommandBody(permission = "NeonRewardPlus.Command.reload")
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.sendMessage("[GeekRewardPlus] 已重新加载配置...")
            RewardManager.load()
            Menu.reload()
        }
    }

    @CommandBody(permission = "NeonRewardPlus.Command.reset")
    val reset = subCommand {
        // pms reset player type key ???
        dynamic("玩家名称") {
            suggest { DataManager.getAllData().map { it.name } }
            execute<CommandSender> { _, context, _ ->
                Bukkit.getPlayer(context["玩家名称"])?.let {
                    it.getBasicData()?.let { data ->
                        data.reset()
                        data.updateByTask()
                    }
                }
            }

            dynamic("种类") {
                suggest { listOf("points", "money", "time") }
                execute<CommandSender> { _, context, _ ->
                    Bukkit.getPlayer(context["玩家名称"])?.let {
                        it.getBasicData()?.let { data ->
                            when (context["种类"]) {
                                "points" -> data.pointsKey.clear()
                                "money" -> data.moneyKey.clear()
                                "time" -> data.timeKey.clear()
                            }
                            data.updateByTask()
                        }
                    }
                }

                dynamic("key") {
                    execute<CommandSender> { _, context, _ ->
                        Bukkit.getPlayer(context["玩家名称"])?.let {
                            val key = context["key"]
                            it.getBasicData()?.let { data ->
                                when (context["种类"]) {
                                    "points" -> data.pointsKey.removeIf { k -> k == key }
                                    "money" -> data.moneyKey.removeIf { k -> k == key }
                                    "time" -> data.timeKey.removeIf { k -> k == key }
                                }
                                data.updateByTask()
                            }
                        }
                    }
                }
            }
        }
    }

    @CommandBody(permission = "NeonRewardPlus.Command.data")
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
                                            data.money += value.toInt()
                                            console().sendLang("Data-Add-value", it.name, "添加", value, "累计金币")
                                            sender.sendLang("Data-Add-value", it.name, "添加", value, "累计金币")
                                        }
                                        else {
                                            data.money -= value.toInt()
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
                                            data.time.merge(ExpIryBuilder(value, false))
                                            console().sendLang("Data-Add-value", it.name, "添加", value, "累计在线")
                                            sender.sendLang("Data-Add-value", it.name, "添加", value, "累计在线")
                                        } else {
                                            data.time.merge(ExpIryBuilder(value, false))
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
            suggest { listOf("points", "money", "time") }
            execute<Player> { sender, text, _ ->
                sender.getBasicData()?.let {
                    when (text["菜单名称"]) {
                        "points" -> sender.openPointsUI(it)
                        "money" -> sender.openMoneyUI(it)
                        "time" -> sender.openTimeUI(it)
                        else -> {
                            sender.sendLang("Open-Menu-Err", text["菜单名称"])
                        }
                    }
                }
            }
        }
    }
}