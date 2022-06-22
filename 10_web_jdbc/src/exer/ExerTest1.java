package exer;

import org.junit.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * @Auther:wangyanan
 * @Date:2022/5/10 15:34
 * @Compriton:
 */
public class ExerTest1 {
    @Test
    public void testInsert() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入id");
        int id = scanner.nextInt();
        System.out.println("输入名字");
        String name = scanner.next();
        String sql = "insert into employee(id,name) values(?,?)";
        int insertCount = update(sql,id,name);
        if (insertCount > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }

    }

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
}
