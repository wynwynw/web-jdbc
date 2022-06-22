package propared_crudtest;

import bean.Order;
import bean.emp;
import org.junit.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther:wangyanan
 * @Date:2022/5/10 14:31
 * @Compriton: 针对不同表的通用的查询操作
 */
public class PreparedStatementQueryTest {
    @Test
    public void testGetForList(){
        String sql = "select id,name from employee where id<?";
        List<emp> list = getForList(emp.class, sql, 3);
        list.forEach(System.out::println);

        String sql1 = "select order_id orderId,order_name orderName from `order` where order_id<?";
        List<Order> forList = getForList(Order.class, sql1, 4);
        forList.forEach(System.out::println);

    }

    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
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
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {
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
                list.add(t);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);

        }
        return null;
    }



    @Test
    public void testGetInstance() {
        String sql = "select id,name from employee where id=?";
        emp instance = getInstance(emp.class, sql, 1);
        System.out.println(instance);

        String sql1 = "select order_id orderId,order_name orderName from `order` where order_id=?";
        Order instance1 = getInstance(Order.class, sql1, 1);
        System.out.println(instance1);
    }

    /*
     * 不同表的通用查询
     * */
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
}
