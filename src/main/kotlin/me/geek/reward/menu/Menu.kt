package me.geek.reward.menu

import me.geek.GeekRewardPlus
import me.geek.GeekRewardPlus.debug
import me.geek.reward.menu.sub.Icon
import me.geek.reward.menu.sub.Micon
import me.geek.reward.menu.sub.Msession
import me.geek.reward.menu.sub.Session
import me.geek.reward.modules.ModulesManage
import me.geek.reward.utils.HexUtils.colorify
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.common.platform.function.releaseResourceFile
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.SecuredFile
import taboolib.module.configuration.util.getMap
import taboolib.module.lang.sendLang
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis

/**
 * 作者: 老廖
 * 时间: 2022/8/19
 */
object Menu {

    @JvmField
    val isOpen: MutableList<Player> = ArrayList()
    // key = 菜单名称 , value = 会话菜单
    private val MenuCache: MutableMap<String, Msession> = HashMap()
    // key = 菜单绑定的命令  value = 菜单名称
    private val MenuCmd: MutableMap<String, String> = HashMap()
    private val AIR = ItemStack(Material.AIR)

    /**
     * 构建GUI菜单
     * @param player 目标玩家
     * @param msession 会话菜单
     * @return Inventory 界面
     */
    @JvmStatic
    fun BuildInventory(player: Player, MenuTag: String): Inventory {
        val menu = MenuCache[MenuTag]!!
        val item = menu.itemStack
        val inventory = Bukkit.createInventory(player, menu.size, menu.title)
        if (item.isNotEmpty()) {
            inventory.contents = item
        }
        return inventory
    }

    /**
     * 关闭所有本插件打开的GUI
     */
    fun CloseGui() {
        Bukkit.getOnlinePlayers().forEach { player ->
            if (isOpen.contains(player)) {
                player.closeInventory()
            }
        }
    }

    /**
     * @param cmd 菜单指令
     * @return 菜单名称
     */
    fun getMenuCommand(cmd: String): String? {
        return MenuCmd[cmd]
    }

    /**
     * @param SessionID 菜单名称
     * @return 会话菜单
     */
    @JvmStatic
    fun getSession(SessionID: String): Msession {
        return MenuCache[SessionID]!!
    }

    fun loadMenu() {
        val list = mutableListOf<File>()
        measureTimeMillis {
            list.also {
                it.addAll(forFile(saveDefaultMenu))
            }
            val icon = mutableListOf<Micon>()
            var menu: SecuredFile
            var menuTag: String
            var title: String
            var type: String
            var layout: String
            var size: Int
            var bindings: String
            list.forEach { file ->
                debug("list: ${file.toPath()}")
                icon.clear()
                menu = SecuredFile.loadConfiguration(file)
                menuTag = file.name.substring(0, file.name.indexOf("."))
                title = colorify(menu.getString("TITLE")!!)
                debug("title: $title")
                type = menu.getString("TYPE")!!
                layout = menu.getStringList("Layout").toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(", ", "")
                size = menu.getStringList("Layout").size * 9
                bindings = menu.getString("Bindings.Commands")!!

                menu.getMap<String, ConfigurationSection>("Icons").forEach { (name, obj) ->
                    icon.add(Icon(name, obj))
                }
                val listIcon = ArrayList<Micon>(icon)
                MenuCache[menuTag] = Session(menuTag, title, layout, size, bindings, listIcon, type, Build(listIcon, layout, size))
                MenuCmd[bindings] = menuTag
            }
        }.also {
            console().sendLang("Loaded-Menu-Points", list.size, it) }
    }

    private val saveDefaultMenu by lazy {
        val dir = File(GeekRewardPlus.instance.dataFolder, "menu")
        if (!dir.exists()) {
            arrayOf(
                "menu/points.yml",
                "menu/money.yml",
                "menu/time.yml"
            ).forEach { releaseResourceFile(it, true) }
        }
        dir
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



    private fun Build(var1: List<Micon>, Layout: String, size: Int): Array<ItemStack> {
        val item = mutableListOf<ItemStack>()
        try {
            var index = 0
            while (index < size) {
                if (Layout[index] != ' ') {
                    val IconID = Layout[index].toString()
                    item.add(index, item(IconID, var1))
                } else {
                    item.add(index, AIR)
                }
                index++
            }
        } catch (ignored: StringIndexOutOfBoundsException) {
        }
        return item.toTypedArray()
    }

    private fun item(id: String, micon: List<Micon>): ItemStack {
        for (icon in micon) {
            if (icon.icon == id) {
                val itemStack = try {
                    if (icon.mats.contains("IA:" , ignoreCase = true) && ModulesManage.itemsAdder.isHook) {
                        val meta = icon.mats.split(":")
                        ModulesManage.itemsAdder.getItem(meta[1])
                    } else {
                        ItemStack(Material.valueOf(icon.mats))
                    }
                } catch (ing: java.lang.IllegalArgumentException) {
                    ItemStack(Material.STONE, 1)
                }
                val itemMeta = itemStack.itemMeta
                if (itemMeta != null) {
                    itemMeta.setDisplayName(icon.name)
                    if (icon.lore.size == 1 && icon.lore[0].isEmpty()) {
                     //   itemMeta.lore = null
                    } else {
                        itemMeta.lore = icon.lore
                    }
                    itemStack.itemMeta = itemMeta
                }
                return itemStack
            }
        }
        return AIR
    }

}