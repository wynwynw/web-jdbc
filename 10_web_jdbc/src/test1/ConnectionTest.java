package test1;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Auther:wangyanan
 * @Date:2022/5/9 12:57
 * @Compriton:
 */
public class ConnectionTest {
    //方式1
    @Test
    public void testConn() throws SQLException {
        //获取Driver的实现类对象
        Driver driver = new com.mysql.jdbc.Driver();

        String url = "jdbc:mysql://localhost:13306/dbtest1?useSSL=false&serverTimezone=UTC";
        //将用户名密码封装在Properties中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }

    //方式2:对方式1的迭代  在如下的程序中不出现第三方的api  使程序具有更好的可移植性
    @Test
    public void testConn2() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        //1.获取Driver实现类对象 使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //2.提供连接的数据库
        String url = "jdbc:mysql://localhost:13306/dbtest1?useSSL=false&serverTimezone=UTC";

        //3.提供连接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");

        //4.获取连接
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }

    //方式3 使用DriverManager替换Driver
    @Test
    public void testConn3() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        //1.获取Driver实现类对象
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        //2.提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:13306/dbtest1?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "123456";
        //3.注册驱动
        DriverManager.registerDriver(driver);

        //4.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    //方式4
    @Test
    public void testConn4() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        //1.提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:13306/dbtest1?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "123456";

        //2.加载Driver
        Class.forName("com.mysql.jdbc.Driver");
        //相较于方式3 省略了如下代码 为什么能省略
        /*
        * 在mysql的Driver中有一段静态代码块
        *   static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
        * */
//        Driver driver=(Driver) clazz.newInstance();

        //4.获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    //方式5 最终版  将数据库连接需要的四个基本信息 放在配置文件中 通过获取配置文件的方式连接
    /*
    * 此方式好处：
    * 实现了数据与代码的分离 实现了解耦
    * 如果需要修改配置文件信息 可以避免程序重新打包
    * */
    @Test
    public void testConn5() throws IOException, ClassNotFoundException, SQLException {
        //读取配置文件中四个配置信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        pros.load(is);
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //2.加载驱动
        Class.forName(driverClass);
        //3.获取连接
        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }
}
