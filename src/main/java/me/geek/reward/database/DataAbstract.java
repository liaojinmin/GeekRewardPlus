package me.geek.reward.database;


import java.sql.Connection;
import java.sql.SQLException;

public abstract class DataAbstract {

    public abstract Connection getConnection() throws SQLException;

    public abstract void loadDataBase();

    public abstract void stopDataBase();

}
