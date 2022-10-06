package me.geek.reward.kether


import me.geek.GeekRewardPlus.say
import me.geek.reward.kether.sub.KetherSub
import me.geek.reward.modules.ModulesManage
import taboolib.library.kether.ParsedAction
import taboolib.library.kether.QuestContext
import taboolib.module.kether.KetherParser
import taboolib.module.kether.scriptParser
import java.util.concurrent.CompletableFuture

/**
 * 作者: 老廖
 * 时间: 2022/8/8
 *
 **/
class totalPoints(private val context: ParsedAction<*>): KetherSub<Boolean>() {

    override fun run(frame: QuestContext.Frame): CompletableFuture<Boolean> {

        return frame.newFrame(context).run<Any>().thenApply {
            val player = getPlayer(frame)
            ModulesManage.hasTotalPoints(player.uniqueId, it.toString().toInt())
        }
    }
    companion object {
        /**
         * totalPoints 100
         */
        @KetherParser(value = ["totalPoints"], namespace = "GeekRewardPlus")
        fun parser() = scriptParser {
            totalPoints(it.nextAction<Any>())
        }
    }
}