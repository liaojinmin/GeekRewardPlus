package me.geek.reward.menu.sub

import org.bukkit.inventory.ItemStack
import taboolib.library.configuration.ConfigurationSection

/**
 * 作者: 老廖
 * 时间: 2022/7/5
 */
data class Session(
    /**
     * 菜单文件名称
     */
    override val session: String,
    override val title: String,
    override val stringLayout: String,
    override val size: Int,
    override val bindings: String,
    override var micon: List<Micon>,
    override val type: String,
    override var itemStack: Array<ItemStack>

) : Msession {
    constructor(session: String, obj: ConfigurationSection, icon: List<Micon>, itemStack: Array<ItemStack>): this(
        session,
        obj.getString("TITLE")!!,
        obj.getStringList("Layout").toString()
            .replace("[", "")
            .replace("]", "")
            .replace(", ", ""),
        obj.getStringList("Layout").size * 9,
        obj.getString("Bindings.Commands")!!,
        icon,
        obj.getString("TYPE")!!,
        itemStack
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Session

        if (session != other.session) return false
        if (title != other.title) return false
        if (stringLayout != other.stringLayout) return false
        if (size != other.size) return false
        if (bindings != other.bindings) return false
        if (micon != other.micon) return false
        if (type != other.type) return false
        if (!itemStack.contentEquals(other.itemStack)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = session.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + stringLayout.hashCode()
        result = 31 * result + size
        result = 31 * result + bindings.hashCode()
        result = 31 * result + micon.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + itemStack.contentHashCode()
        return result
    }
}