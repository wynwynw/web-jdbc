package propared_crudtest;

import bean.emp;
import org.junit.Test;
import util.JDBCUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

/**
 * @Auther:wangyanan
 * @Date:2022/5/9 17:48
 * @Compriton: 针对于employee表的查询操作
 */
public class Employee {
    @Test
    public void testQueryForEmp(){
        String sql="select id,name from employee where id=?";
        emp emp = queryEmp(sql, 2);
        System.out.println(emp);
    }
    //通用的查询操作
    public emp queryEmp(String sql, Object... args) {
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
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                emp emp1 = new emp();
                for (int i = 0; i < columnCount; i++) {
                    Object columnvalue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);
                    //给emp1对象指定的columnName属性，赋值为columnvalue  通过反射
                    Field field = emp.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(emp1,columnvalue);
                }
                return emp1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            JDBCUtils.closeResource(conn,ps,rs);

        }
        return null;
    }

    @Test
    public void testquery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name from employee where id=?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);
            //执行并返回结果集
            resultSet = ps.executeQuery();

            //处理结果集
            if (resultSet.next()) {//判断结果集下一条是否有数据  有数据返回true 指针下移  返回false 指针不会下移
                //获取当前这条数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);

                //方式1
                //System.out.println("id="+id+"name="+name);
                //方式2
                //Object[] data=new Object[]{id,name};
                //方式三 把数据封装为对象
                emp emp = new emp(id, name);
                System.out.println(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, resultSet);

        }
    }
}
