package me.geek.reward.kether.impl

import me.geek.reward.kether.KetherSub
import me.geek.reward.api.DataManager
import taboolib.library.kether.ParsedAction
import taboolib.library.kether.QuestContext
import taboolib.module.kether.KetherParser
import taboolib.module.kether.scriptParser
import java.util.concurrent.CompletableFuture

/**
 * 作者: 老廖
 * 时间: 2022/8/26
 *
 **/
class pointsPack(private val context: ParsedAction<*>): KetherSub<Boolean>() {
    override fun run(frame: QuestContext.Frame): CompletableFuture<Boolean> {
        return frame.newFrame(context).run<Any>().thenApply {
            val player = getPlayer(frame)
            val data = DataManager.getBasicData(player.uniqueId) ?: DataManager.getBasicData(player.name)
            data?.pointsKey?.contains(it.toString())
        }
    }
    companion object {
        /**
         * pointsPack *ID
         */
        @KetherParser(value = ["pointsPack"], namespace = "GeekRewardPlus")
        fun parser() = scriptParser {
            pointsPack(it.nextAction<Any>())
        }
    }
}