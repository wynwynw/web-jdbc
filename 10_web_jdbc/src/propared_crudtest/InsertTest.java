package propared_crudtest;

import org.junit.Test;
import util.JDBCUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Auther:wangyanan
 * @Date:2022/5/10 18:52
 * @Compriton: 使用PreparedStatement实现批量数据的操作  指的是插入操作
 * 像goods表插入两万条数据
 */

public class InsertTest {
    @Test
    public void testInsert1() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values (?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 20000; i++) {
                ps.setObject(1, "name_" + i);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间：" + (end - start));//花费的时间：958685
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);

        }
    }

    //批量操作三
    //addBatch() excuteBatch() clearBatch()
    /*
     * Mysql服务器默认是关闭批处理的 我们需要一个参数 让Mysql开启批处理的支持
     * ?rewriteBatchedStatements=true  写在配置文件的url后面
     * */
    @Test
    public void testInsert2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values (?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 20000; i++) {
                ps.setObject(1, "name_" + i);
                //1.攒sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //2.执行
                    ps.executeBatch();
                    //3.清空batch
                    ps.clearBatch();
                }
                //   ps.execute();

            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间：" + (end - start));//花费的时间：6253
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);

        }
    }

    //批量操作方式四
    @Test
    public void testInsert3() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            //设置不允许自动提交数据
            conn.setAutoCommit(false);

            String sql = "insert into goods(name) values (?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 20000; i++) {
                ps.setObject(1, "name_" + i);
                //1.攒sql
                ps.addBatch();
                if (i % 500 == 0) {
                    //2.执行
                    ps.executeBatch();
                    //3.清空batch
                    ps.clearBatch();
                }
                //   ps.execute();

            }
            //提交数据
            conn.commit();

            long end = System.currentTimeMillis();
            System.out.println("花费的时间：" + (end - start));//花费的时间：4416
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);

        }
    }
}
