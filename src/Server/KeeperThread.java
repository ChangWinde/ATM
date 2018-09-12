package Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KeeperThread extends Thread{
    private Statement statement;
    private ServerThread st;
    private int name;
    private final int TIME = 5000;
    private HashMap<Double,Long> info = new HashMap<Double,Long>();

    public KeeperThread(int name,ServerThread st,Statement statement){
        this.name = name;
        this.st = st;
        this.statement = statement;
        System.out.println("我是管家of"+name);
        //主要业务
        run();
    }
    public void run(){
        try {
            sleep(TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //计息
        calculateInterest();
        //更新状态
        st.setCount(0);
        //自动进行还钱操作
        if(!st.getStatePay())
            autoPay();
        st.setStatePay(false);
    }
    //自动还钱
    private void autoPay() {
        ResultSet resultSet1 = null;
        try {
            resultSet1 = statement.executeQuery("SELECT * FROM ItemTable WHERE type="+3+" AND name="+"'"+name+"'");
            if(resultSet1.next()) {
                double money = resultSet1.getDouble("money")/10;
                ResultSet resultSet2 = statement.executeQuery("SELECT * FROM ItemTable WHERE (type=1 OR type=2)"+" AND name="+"'"+name+"'"+"LIMIT 0,1");
                Long date = resultSet2.getLong("date");
                int type = resultSet2.getInt("type");
                if(money>resultSet2.getDouble("money"))
                    statement.executeUpdate("DELETE FROM ItemTable WHERE name="+"'"+name+"'"+" AND type="+type+" AND date="+date);
                else {
                    money -= resultSet2.getDouble("money");
                    statement.executeUpdate("UPDATE ItemTable SET money=" + money + " WHERE type=" + type + " AND name=" + "'" + name + "'"+" AND date="+date);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //计算利息
    public void calculateInterest(){
        try {
            //临时信息处理
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ItemTable WHERE name="+name);
            while(resultSet.next()){
                double money = resultSet.getDouble("money")*(1+resultSet.getFloat("rate"));
                Long date = resultSet.getLong("date");
                info.put(money,date);
            }
            Iterator it = info.entrySet().iterator();
            //遍历哈希表
            while(it.hasNext()){
                Map.Entry entry = (Map.Entry)it.next();
                Double money = (Double)entry.getKey();
                Long date1 = (Long)entry.getValue();
                Date date2 = new Date();
                statement.executeUpdate("UPDATE ItemTable SET money="+money+",date="+date2.getTime()+" WHERE name="+"'"+name+"'"+" AND date="+date1);
            }
            //情况哈希表
            info.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
