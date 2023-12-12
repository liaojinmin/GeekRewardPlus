package me.neon.reward.api.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ExpIryBuilder(
    /**
     * 入参
     * 1d1h30m42s
     * 1天4时30秒10m
     */
    @SerializedName("t")
    private val time: String,
    /**
     * 时间戳计时方法，默认倒计时
     */
    @SerializedName("i")
    private val isDown: Boolean = true
) {
    @SerializedName("m")
    var millis: Long = -1L
        private set

    @Expose
    private val cache: MutableMap<String, Long> = mutableMapOf<String, Long>().apply {
        if (time.isEmpty()) return@apply
        val regex = Regex("\\d+?(?i)(d|h|m|s)\\s?")
        regex.findAll(time).forEach {
            this[it.groupValues[1]] = it.groupValues[0].substringBefore(it.groupValues[1]).toLong()
        }
    }
    /**
     * 首次初始化 millis
     */
    init {
        if (millis == -1L) {
            if (this.cache.isEmpty()) millis = 0
            val d = cache["d"]?.let { it * 24 * 60 * 60 } ?: 0
            val h = cache["h"]?.let { it * 60 * 60 } ?: 0
            val m = cache["m"]?.let { it * 60 } ?: 0
            // 获取到期时间
            val ac = d + h + m + (cache["s"] ?: 0)
            millis = ac
        }
    }

    /**
     * 获取 timedata 的格式化单位
     * 默认正计时 基于初始化语言选择后辍
     * @return 获得格式 - 00d 00h 00m 00s
     */
    fun getExpiryFormat(): String {
        val times = millis
        var text = ""
        val dd = times / 60 / 60 / 24
        val hh = times / 60 / 60 % 24
        val mm = times / 60 % 60
        val ss = times % 60
        if (dd > 0) text += "${dd}天 "
        if (hh > 0) text += "${hh}小时 "
        if (mm > 0) text += "${mm}分钟 "
        if (ss > 0) text += "${ss}秒"
        return text.ifEmpty { "0" }
    }

    /**
     * 根据计时种类更新时间戳
     * @param time = 秒
     */
    fun autoUpdate(time: Long = 1) {
        if (!isDown) {
            millis+=time
        } else {
            millis-=time
        }
    }

    fun setMillis(time: Long): ExpIryBuilder {
        this.millis = time
        return this
    }

    fun merge(expIryBuilder: ExpIryBuilder): ExpIryBuilder {
        this.millis += expIryBuilder.millis
        return this
    }
}