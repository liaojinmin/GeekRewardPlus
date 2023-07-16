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
 * 时间: 2022/8/30
 *
 **/
class timePack(private val context: ParsedAction<*>): KetherSub<Boolean>() {
    override fun run(frame: QuestContext.Frame): CompletableFuture<Boolean> {
        return frame.newFrame(context).run<Any>().thenApply {
            val player = getPlayer(frame)
            val data = DataManager.getBasicData(player.uniqueId) ?: DataManager.getBasicData(player.name)
            data?.timeKey?.contains(it.toString()) ?: false
        }
    }
    companion object {
        /**
         * moneyPack *ID
         */
        @KetherParser(value = ["timePack"], namespace = "GeekRewardPlus")
        fun parser() = scriptParser {
            timePack(it.nextAction<Any>())
        }
    }
}