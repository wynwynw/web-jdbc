package exer;

import bean.Order;
import org.junit.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @Auther:wangyanan
 * @Date:2022/5/10 16:10
 * @Compriton:
 */
public class ExerTest2 {
    @Test
    public void testInsert() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入id");
        int id = scanner.nextInt();
        System.out.println("输入名字");
        String name = scanner.next();
        System.out.println("输入日期");
        String date = scanner.next();
        String sql = "insert into `order`(order_id,order_name,order_date) values(?,?,?)";
        int count = update(sql, id, name, date);
        if (count > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }

    //添加
    //通用的增删改查操作
    public int update(String sql, Object... args) {
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
            // ps.execute();
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);

        }
        //return false;
        return 0;
    }

    //根据id查询信息
    @Test
    public void queryId() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入id号");
        int id = scanner.nextInt();
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id=?";
        Order instance = getInstance(Order.class, sql, id);
        System.out.println(instance);
    }

    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            //获取结果集元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名  getColumnName()
                    //获取列的别名 getColumnLabel()
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射 将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);

        }
        return null;
    }

    //根据id删除信息
    @Test
    public void testDeleteId() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入order_id:");
        int id = scanner.nextInt();
        //先查询指定 id信息
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id=?";

        Order instance = getInstance(Order.class, sql, id);
        if (instance == null) {
            System.out.println("查无此信息 请重新输入");
        } else {
            String sql1 = "delete from `order` where order_id=?";
            int del = update(sql1, id);
            if (del > 0) {
                System.out.println("删除成功");
            }
        }
    }
    //优化删除
    @Test
    public void testDeleteId1() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入order_id:");
        int id = scanner.nextInt();
        String sql1 = "delete from `order` where order_id=?";
        int del = update(sql1, id);
        if (del > 0) {
            System.out.println("删除成功");
        }else {
            System.out.println("查无此信息 请重新输入");
        }
    }

}
