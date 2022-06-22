package propared_crudtest;

import org.junit.Test;
import test1.ConnectionTest;
import util.JDBCUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Auther:wangyanan
 * @Date:2022/5/9 15:23
 * @Compriton:
 */
/*
 * 使用PreparedStatement来替换Statement 实现对数据表的增删改查操作
 * */
public class ProparedTest1 {
    @Test
    public void testCommonupdate1() {
        String sql = "delete from employee where id=?";
        update(sql, 3);
    }

    //通用的增删改查操作
    public void update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //获取数据库连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句 返回PreparedStatement实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);

        }
    }

    //修改employee一条记录
    @Test
    public void testupdate() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //获取数据库连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句 返回PreparedStatement实例
            String sql = "update employee set name=? where id=?";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1, "莫扎特");
            ps.setObject(2, 3);
            //4.执行sql
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源关闭
            JDBCUtils.closeResource(conn, ps);
        }

    }

    //向employee表中添加一条记录
    @Test
    public void test1() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
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
            conn = DriverManager.getConnection(url, user, password);
            //System.out.println(conn);

            //4.预编译sql语句返回prepareStatement实例
            String sql = "insert into employee values (?,?)";//?  占位符
            ps = conn.prepareStatement(sql);
            //填充占位符
            ps.setInt(1, 3);
            ps.setString(2, "泰坦尼克号");

            //执行sql
            ps.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //资源关闭
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

    }
}
