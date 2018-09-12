package Server;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;

public class ServerThread extends Thread{
    //所连接的socket
    private Socket socket;
    //输入输出流
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Statement statement = null;
    //临时信息
    private int count = 0;
    private int name;
    private boolean statePay = false;
    //管家线程
    private KeeperThread keeperThread = null;
    public ServerThread(Socket socket, Statement statement){
        //初始化socket,statement
        this.socket = socket;
        this.statement = statement;
        //更新连接数
        ServerDaemon.currentThread++;
        //初始化流
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //运行
        prepare();
    }

    //登录注册前期工作************************
    //准备
    public void prepare(){
        String message = null;
        while(true){
            try {
                message = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String infoList[] = message.split("&");
            this.name = infoList[1].hashCode();
            //区分过程
            if(infoList[0].equals("0")){
                //登录过程
                if(verify(name,infoList[2].hashCode()))
                    break;
            }else{
                //注册过程
                if(register(name,infoList[2].hashCode()))
                    break;
            }
        }
        //启动管家线程
        keeperThread = new KeeperThread(name,this,statement);
        keeperThread.start();
        //运行业务逻辑
        run();
    }
    //验证登录
    public boolean verify(int name,int psd){
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM AccountTable WHERE name='"+name+"' AND psd='"+psd+"'");
            if(resultSet.next()) {
                out.println("Y");
                return true;
            }
            else{
                out.println("N");
                return false;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //用户注册
    public boolean register(int name,int psd){
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM AccountTable WHERE name='"+name+"'");
            if(resultSet.next()) {
                out.println("N");
                return false;
            }
            else {
                out.println("Y");
                //加入数据库
                System.out.println("INSERT INTO AccountTable(name,psd,state)VALUES("+"'"+name+"'"+","+"'"+psd+"'"+","+"NULL"+")");
                statement.executeUpdate("INSERT INTO AccountTable(name,psd,state)VALUES("+"'"+name+"'"+","+"'"+psd+"'"+","+"NULL"+")");
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //主要业务*******************************
    //逻辑业务循环
    public void run(){
        String fromClient = null;
        while (true){
            //读取消息
            try {
                fromClient = in.readLine();
            }catch (IOException e){
                e.printStackTrace();
            }
            if(fromClient != null){
                //解析服务内容
                String info[] = fromClient.split("#");
                System.out.println("接受客户端消息"+info[1]);
                //业务类型
                String protocol = info[0];
                //交易金额
                double money = 123;
                //进入服务列表
                switch (protocol){
                    case "3"://存钱
                        save(info[1]);
                        count++;
                        break;
                    case "4"://取钱
                        withdraw(info[1]);
                        count++;
                        break;
                    case "5"://查询余额
                        query(info[1]);
                        count++;
                        break;
                    case "6"://贷款
                        loan(info[1]);
                        break;
                    case "7"://还钱
                        payLoan(info[1]);
                        break;
                    case "8"://开通储蓄账户
                        openAccount(info[1]);
                        break;
                }
            }
        }
    }

    //存钱
    private void save(String message){
        String [] infoList = message.split("&");
        int name = infoList[0].hashCode();
        String type = infoList[1];
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM ItemTable WHERE type="+type+" AND name="+"'"+name+"'"+" LIMIT 0,1");
            if(type.equals("2")) {//支票账户
                if(!resultSet.next()){
                    java.util.Date date = new Date();
                    statement.executeUpdate("INSERT INTO ItemTable VALUES(" + "'" + name + "'" + "," + type + "," + infoList[2] + "," + infoList[3] + "," + date.getTime()+")");
                } else{
                    double money = resultSet.getDouble("money")+Double.parseDouble(infoList[2]);
                    statement.executeUpdate("UPDATE ItemTable SET money="+money+" WHERE type="+type+" AND name="+"'"+name+"'");
                }
            }
            else{//储蓄账户
                if(resultSet.next()){
                    double money = resultSet.getDouble("money")+Double.parseDouble(infoList[2]);
                    String date = resultSet.getString("date");
                    statement.executeUpdate("UPDATE ItemTable SET money="+money+" WHERE date="+date+" AND type="+type+" AND name="+"'"+name+"'");
                }
            }

            //回传消息
            out.println("Y");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //取钱
    private  void withdraw(String message){
        String [] infoList = message.split("&");
        int name = infoList[0].hashCode();
        String type = infoList[1];
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM ItemTable WHERE type="+type+" AND name="+"'"+name+"'"+" LIMIT 0,1");
            //验证结果集
            if(resultSet.next()){
                double money = resultSet.getDouble("money")-Double.parseDouble(infoList[2]);

                if(money >= 0){//余额充足
                    String date = resultSet.getString("date");
                    statement.executeUpdate("UPDATE ItemTable SET money="+money+" WHERE date="+date+" AND type="+type+" AND name="+"'"+name+"'");
                    //回传消息
                    out.println("Y");
                }else{//余额不足
                    out.println("N");
                }
            }else{
                out.println("N");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //查询余额
    private void query(String message){
        String [] infoList = message.split("&");
        int name = infoList[0].hashCode();
        String type = infoList[1];
        ResultSet resultSet = null;
        try {
            System.out.println("SELECT * FROM ItemTable WHERE type="+type+" AND name="+"'"+name+"'"+" LIMIT 0,1");
            resultSet = statement.executeQuery("SELECT * FROM ItemTable WHERE type="+type+" AND name="+"'"+name+"'"+" LIMIT 0,1");
            if(resultSet.next()){
                double money = resultSet.getDouble("money");
                //截取小数
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                //回传消息
                out.println(decimalFormat.format(money));
            }else
                out.println("没有开户！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //开储蓄账户项目
    private void openAccount(String message){
        String infoList[] = message.split("&");
        try {
            statement.executeUpdate("INSERT INTO ItemTable"+" VALUES("+"'"+infoList[0].hashCode()+"'"+","+infoList[1]+","+infoList[2]+","+infoList[3]+","+infoList[4]+")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //贷款
    private void loan(String message){
        if(count<3)
            out.println("N1");//非活跃账户
        //贷款服务处理
        String [] infoList = message.split("&");
        int name = infoList[0].hashCode();
        try {
            //计算金额
            double money = 0.0;
            ResultSet resultSet1 = statement.executeQuery("SELECT money FROM ItemTable WHERE type IN (1,2)");
            while(resultSet1.next()){
                money += resultSet1.getDouble("money");
            }
            //判断是否超额
            if(money<Double.parseDouble(infoList[2]))
                out.println("N2");
            else{
                ResultSet resultSet2 = statement.executeQuery("SELECT * FROM ItemTable WHERE type="+3+" AND name="+"'"+name+"'"+" LIMIT 0,1");
                if(!resultSet2.next()){
                    java.util.Date date = new Date();
                    statement.executeUpdate("INSERT INTO ItemTable VALUES(" + "'" + name + "'" + "," + 3 + "," + infoList[2] + "," + infoList[3] + "," + date.getTime()+")");
                    out.println("Y");
                }else{
                    //存在贷款
                    out.println("N3");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //还钱
    private void payLoan(String message){
        String [] infoList = message.split("&");
        int name = infoList[0].hashCode();
        String type = infoList[1];
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM ItemTable WHERE type="+type+" AND name="+"'"+name+"'");
            if(resultSet.next()) {
                double money = resultSet.getDouble("money");
                if (money/10 > Double.parseDouble(infoList[2]))
                    out.println("N");
                else{
                    //本年已经还钱
                    statePay = true;

                    money -=  Double.parseDouble(infoList[2]);
                    if(money>0) {//未还请
                        statement.executeUpdate("UPDATE ItemTable SET money=" + money + " WHERE type=" + type + " AND name=" + "'" + name + "'");
                        out.println("Y1");
                    }else{//还款剩余
                        //删除贷款记录
                        statement.executeUpdate("DELETE FROM ItemTable WHERE type="+type+" AND name="+"'"+name+"'");
                        //将多余的钱存入到支票账户中
                        resultSet = statement.executeQuery("SELECT * FROM ItemTable WHERE type=2"+" AND name="+"'"+name+"'");
                        if(!resultSet.next()){
                            java.util.Date date = new Date();
                            statement.executeUpdate("INSERT INTO ItemTable VALUES(" + "'" + name + "'" + ",2," + -money + "," + "0.0015" + "," + date.getTime()+")");
                        } else{
                            money = resultSet.getDouble("money")+(-money);
                            statement.executeUpdate("UPDATE ItemTable SET money="+money+" WHERE type=2"+" AND name="+"'"+name+"'");
                        }
                        out.println("Y2");
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //辅助函数************************************
    //更新状态
    public void setCount(int count){this.count = count;}
    //获取状态码
    public boolean getStatePay(){return statePay;}
    //设置状态码
    public void setStatePay(boolean statePay){this.statePay = statePay;}
}
