package me.geek.reward.database;

import com.zaxxer.hikari.HikariDataSource;
import me.geek.GeekRewardPlus;
import me.geek.reward.configuration.ConfigManager;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 作者: 老廖
 * 时间: 2022/8/28
 **/
public final class Sqlite extends DataAbstract {

    private HikariDataSource sql;
    @Override
    public Connection getConnection() throws SQLException {
        return this.sql.getConnection();
    }

    @Override
    public void loadDataBase() {
        final File data = new File(GeekRewardPlus.INSTANCE.getInstance().getDataFolder(), "data");
        if (!data.exists()) {
            data.mkdirs();
        }
        final String SqliteUrl = "jdbc:sqlite:" + data + File.separator +"GeekData.db";
        sql = new HikariDataSource();
        sql.setDataSourceClassName("org.sqlite.SQLiteDataSource");
        sql.addDataSourceProperty("url", SqliteUrl);
        //附件参数
        sql.setMaximumPoolSize(ConfigManager.MAXIMUM_POOL_SIZE);
        sql.setMinimumIdle(ConfigManager.MINIMUM_IDLE);
        sql.setMaxLifetime(ConfigManager.MAXIMUM_LIFETIME);
        sql.setKeepaliveTime(ConfigManager.KEEPALIVE_TIME);
        sql.setConnectionTimeout(ConfigManager.CONNECTION_TIMEOUT);
        sql.setPoolName("Geek-sqlite");
        createSqliteTables();
    }

    @Override
    public void stopDataBase() {
        if (sql != null) sql.close();
    }

    private void createSqliteTables() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()){
                statement.addBatch("PRAGMA foreign_keys = ON;");
                statement.addBatch("PRAGMA encoding = 'UTF-8';");
                statement.addBatch("CREATE TABLE IF NOT EXISTS " + ConfigManager.MYSQL_Player_Data_NAME + " (" +
                        " `id` integer PRIMARY KEY, " +
                        " `name` VARCHAR(256) NOT NULL, " +
                        " `uuid` CHAR(36) NOT NULL DEFAULT '旧数据', " +
                        " `points` integer NOT NULL DEFAULT '0', " +
                        " `money` Double NOT NULL DEFAULT '0', " +
                        " `time` BIGINT(20) NOT NULL DEFAULT '0', " +
                        " `points_key` VARCHAR(256) NOT NULL DEFAULT '', " +
                        " `money_key` VARCHAR(256) NOT NULL DEFAULT '', " +
                        " `time_key` VARCHAR(256) NOT NULL DEFAULT '');");
                statement.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
