package bean;

/**
 * @Auther:wangyanan
 * @Date:2022/5/9 18:07
 * @Compriton:  orm对象关系映射  一个数据表对应一个java类 表中的一条记录对应java类的一个对象 表中的一个字段 对应java类的一个属性
 */
public class emp {
    private int id;
    private String name;

    public emp() {
    }

    public emp(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "emp{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
