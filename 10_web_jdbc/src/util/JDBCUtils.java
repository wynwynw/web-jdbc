package util;

import test1.ConnectionTest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @Auther:wangyanan
 * @Date:2022/5/9 15:49
 * @Compriton:
 */

/*
 * 操作数据库工具类
 * */
public class JDBCUtils {
    public static Connection getConnection() throws IOException, SQLException, ClassNotFoundException {

        //读取配置文件中四个配置信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        pros.load(is);
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //2.加载驱动
        Class.forName(driverClass);
        //3.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    /*
     * 关闭连接和Statement操作
     * */
    public static void closeResource(Connection conn, Statement ps) {
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //关闭
    public static void closeResource(Connection conn, Statement ps,ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs != null)
                rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
