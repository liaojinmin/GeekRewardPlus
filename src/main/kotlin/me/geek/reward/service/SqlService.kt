package me.geek.reward.service

import me.geek.reward.GeekRewardPlus
import java.sql.Connection
import java.sql.SQLException

/**
 * 作者: 老廖
 * 时间: 2022/10/16
 *
 **/
abstract class SqlService {

    abstract var isActive: Boolean


    abstract fun getConnection(): Connection

    abstract fun onStart()

    abstract fun onClose()


    fun createTab(func: SqlService.() -> Unit) {
        try {
            func(this)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
    fun testSql(): Boolean {
        return try {
            GeekRewardPlus.debug("尝试链接数据库...")
            getConnection()
            true
        } catch (e: SQLException) {
            e.printStackTrace()
            GeekRewardPlus.debug("    数据库链接失败...")
            false
        }
    }
}