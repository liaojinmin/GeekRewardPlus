package me.neon.reward.service

import java.io.File

/**
 * 作者: 老廖
 * 时间: 2022/10/16
 *
 **/
data class ConfigSql(
    val type: String = "sqlite",
    val keyModule: String = "UUID",
    val mysql: ConfigMysql = ConfigMysql(),
    val hikari_settings: ConfigHikari = ConfigHikari(),
) {
    var sqlite: File? = null
}
