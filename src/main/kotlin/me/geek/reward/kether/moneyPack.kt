package me.geek.reward.kether

import me.geek.reward.kether.sub.KetherSub
import me.geek.reward.modules.ModulesManage
import taboolib.library.kether.ParsedAction
import taboolib.library.kether.QuestContext
import taboolib.module.kether.KetherParser
import taboolib.module.kether.scriptParser
import java.util.concurrent.CompletableFuture

/**
 * 作者: 老廖
 * 时间: 2022/8/28
 *
 **/
class moneyPack(private val context: ParsedAction<*>): KetherSub<Boolean>() {

    override fun run(frame: QuestContext.Frame): CompletableFuture<Boolean> {
        return frame.newFrame(context).run<Any>().thenApply {
            val data = ModulesManage.getPlayerData(getPlayer(frame).uniqueId)
            data?.Money_key?.contains(it.toString()) ?: false
        }
    }

    companion object {
        /**
         * moneyPack *ID
         */
        @KetherParser(value = ["moneyPack"], namespace = "GeekRewardPlus")
        fun parser() = scriptParser {
            moneyPack(it.nextAction<Any>())
        }
    }
}