package me.geek.reward.kether

import me.geek.GeekRewardPlus
import me.geek.reward.kether.sub.KetherSub
import org.bukkit.Material
import taboolib.library.kether.ParsedAction
import taboolib.library.kether.QuestContext
import taboolib.library.xseries.XMaterial
import taboolib.module.kether.KetherParser
import taboolib.module.kether.isInt
import taboolib.module.kether.scriptParser
import taboolib.platform.util.buildItem
import taboolib.platform.util.giveItem
import java.util.concurrent.CompletableFuture

/**
 * 作者: 老廖
 * 时间: 2022/11/2
 *
 **/
class giveItem(private val mate: String, private val context: ParsedAction<*>): KetherSub<Boolean>() {

    override fun run(frame: QuestContext.Frame): CompletableFuture<Boolean> {
        return frame.newFrame(context).run<Any>().thenApply {
            val item = buildItem(XMaterial.STONE) {
                setMaterial(XMaterial.valueOf(mate.uppercase()))
                amount = if (it.toString().isInt()) it.toString().toInt() else 1
            }
            GeekRewardPlus.debug(item.type.name)
            getPlayer(frame).giveItem(item)
            item.type != Material.STONE
        }
    }

    companion object {
        /**
         * giveItem *ID 1
         */
        @KetherParser(value = ["giveItem"], namespace = "GeekRewardPlus")
        fun parser() = scriptParser {
            giveItem(it.nextToken(), it.nextAction<Any>())
        }
    }
}