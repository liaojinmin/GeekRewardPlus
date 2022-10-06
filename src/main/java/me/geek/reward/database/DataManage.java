package me.geek.reward.database;

import me.geek.reward.configuration.ConfigManager;

import java.sql.*;

/**
 * 作者: 老廖
 * 时间: 2022/8/18
 **/
public final class DataManage {

    private static DataAbstract dataAbstract;

    public static Connection getConnection() throws SQLException {
        return dataAbstract.getConnection();
    }
    public static void close() {
        dataAbstract.stopDataBase();
    }
    public static void start() {
        if (ConfigManager.SQL_TYPE.equals("sqlite")) {
            dataAbstract = new Sqlite();
        } else {
            dataAbstract = new Mysql();
        }
        dataAbstract.loadDataBase();
    }


}
