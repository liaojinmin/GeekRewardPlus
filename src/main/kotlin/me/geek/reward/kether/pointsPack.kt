package me.geek.reward.kether

import me.geek.reward.kether.sub.KetherSub
import me.geek.reward.modules.ModulesManage
import me.geek.reward.modules.sub.PlayersData
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
            val data = ModulesManage.getPlayerData(getPlayer(frame).uniqueId)
            data?.Points_key?.contains(it.toString())
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