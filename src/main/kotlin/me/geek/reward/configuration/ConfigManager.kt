package me.geek.reward.configuration

import me.geek.reward.configuration.RewardFiles.config
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import kotlin.system.measureTimeMillis

/**
 * 作者: 老廖
 * 时间: 2022/8/18
 */
object ConfigManager {

    fun loadConfig() {
        measureTimeMillis {
            DeBug = config.getBoolean("debug", false)
            //sql
            SQL_TYPE = config.getString("data_storage.type", "sqlite")!!
            MYSQL_HOST = config.getString("data_storage.mysql.host", "127.0.0.1")!!
            MYSQL_PORT = config.getInt("data_storage.mysql.port", 3306)
            MYSQL_DATABASE = config.getString("data_storage.mysql.database", "server_reward")!!
            MYSQL_USERNAME = config.getString("data_storage.mysql.username", "root")!!
            MYSQL_PASSWORD = config.getString("data_storage.mysql.password", "123456")!!
            MYSQL_PARAMS = config.getString("data_storage.mysql.params", "?autoReconnect=true&useSSL=false")!!
            MYSQL_Player_Data_NAME = config.getString("data_storage.mysql.Player_Data", "player_data")!!
            MYSQL_Pack_Data_NAME = config.getString("data_storage.mysql.Pack_Data", "pack_data")!!
            // hikari
            MAXIMUM_POOL_SIZE = config.getInt("data_storage.hikari_settings.maximum_pool_size", 10)
            MINIMUM_IDLE = config.getInt("data_storage.hikari_settings.minimum_idle", 5)
            MAXIMUM_LIFETIME = config.getInt("data_storage.hikari_settings.maximum_lifetime", 1800000)
            KEEPALIVE_TIME = config.getInt("data_storage.hikari_settings.keepalive_time", 0)
            CONNECTION_TIMEOUT = config.getInt("data_storage.hikari_settings.connection_timeout", 5000)
            OK = config.getString("状态信息.已领取", "§a已领取")!!.replace("&", "§")
            YES = config.getString("状态信息.可领取", "§a可领取")!!.replace("&", "§")
            NO = config.getString("状态信息.不可领取", "§c不可领取")!!.replace("&", "§")
        }.also { console().sendLang("Loaded-Config", it)}
    }

    var DeBug = false
    // MYSQL
    lateinit var SQL_TYPE: String
    lateinit var MYSQL_HOST: String
    @JvmField
    var MYSQL_PORT = 0
    lateinit var MYSQL_DATABASE: String
    lateinit var MYSQL_USERNAME: String
    lateinit var MYSQL_PASSWORD: String
    lateinit var MYSQL_PARAMS: String
    lateinit var MYSQL_Player_Data_NAME: String
    lateinit var MYSQL_Pack_Data_NAME: String
    // hikari
    @JvmField
    var MAXIMUM_POOL_SIZE = 0
    @JvmField
    var MINIMUM_IDLE = 0
    @JvmField
    var MAXIMUM_LIFETIME = 0
    @JvmField
    var KEEPALIVE_TIME = 0
    @JvmField
    var CONNECTION_TIMEOUT = 0
    // 已领取
    lateinit var OK: String
    // 可领取
    lateinit var YES: String
    // 不可领取
    lateinit var NO: String
}