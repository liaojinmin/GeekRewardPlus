package me.geek.reward.database;

import com.zaxxer.hikari.HikariDataSource;
import me.geek.reward.configuration.ConfigManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 作者: 老廖
 * 时间: 2022/8/18
 **/
public final class Mysql extends DataAbstract {
    private HikariDataSource sql;

    public Connection getConnection() throws SQLException {
        return this.sql.getConnection();
    }


    public void loadDataBase() {
        final String MysqlUrl = "jdbc:mysql://" + ConfigManager.MYSQL_HOST + ":" + ConfigManager.MYSQL_PORT + "/" + ConfigManager.MYSQL_DATABASE + ConfigManager.MYSQL_PARAMS;
        sql = new HikariDataSource();
        sql.setJdbcUrl(MysqlUrl);
        sql.setUsername(ConfigManager.MYSQL_USERNAME);
        sql.setPassword(ConfigManager.MYSQL_PASSWORD);
        // 设置驱动
        try {
            sql.setDriverClassName("com.mysql.cj.jdbc.Driver");
        } catch (RuntimeException | NoClassDefFoundError e) {
            sql.setDriverClassName("com.mysql.jdbc.Driver");
        }
        sql.setMaximumPoolSize(ConfigManager.MAXIMUM_POOL_SIZE);
        sql.setMinimumIdle(ConfigManager.MINIMUM_IDLE);
        sql.setMaxLifetime(ConfigManager.MAXIMUM_LIFETIME);
        sql.setKeepaliveTime(ConfigManager.KEEPALIVE_TIME);
        sql.setConnectionTimeout(ConfigManager.CONNECTION_TIMEOUT);
        sql.setPoolName("Geek-mysql");
        createMysqlTables();
    }

    public void stopDataBase() {
        if (sql != null) sql.close();
    }

    private void createMysqlTables() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()){
                statement.execute("CREATE TABLE IF NOT EXISTS " + ConfigManager.MYSQL_Player_Data_NAME + " (" +
                        " `id` INT(36) NOT NULL AUTO_INCREMENT, " +
                        " `name` VARCHAR(256) NOT NULL, " +
                        " `uuid` CHAR(36) NOT NULL DEFAULT '旧数据', " +
                        " `points` integer NOT NULL DEFAULT '0', " +
                        " `money` Double NOT NULL DEFAULT '0', " +
                        " `time` BIGINT(20) NOT NULL DEFAULT '0', " +
                        " `points_key` VARCHAR(256) NOT NULL DEFAULT '', " +
                        " `money_key` VARCHAR(256) NOT NULL DEFAULT '', " +
                        " `time_key` VARCHAR(256) NOT NULL DEFAULT '', " +
                        " PRIMARY KEY (`id`))ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
