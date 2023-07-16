package me.geek.reward.listener


/**
 * 作者: 老廖
 * 时间: 2022/8/19
 *
 **/
object PointsListener {

    /*
    @SubscribeEvent
    fun onChange(e: PlayerPointsChangeEvent) {
        val player = Bukkit.getOfflinePlayer(e.playerId)
        val points = e.change
        if (points >= 0) {
            ModulesManage.getPlayerData(player.name!!)?.let {
                if (it.points <= 0) {
                    it.points = points
                } else {
                    it.points = (it.points + points)
                }
                Bukkit.getScheduler().scheduleAsyncDelayedTask(GeekRewardPlus.instance) {
                    ModulesManage.update(it)
                }
            }
        }
    }

     */
}