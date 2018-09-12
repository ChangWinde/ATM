package Server;


import GUI.ServerVisual;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ServerDaemon {
    //被监听端口
    public static final int PORT = 8080;
    //最大线程数
    public static final int MAXTHREAD= 6;
    //现在所存在的线程数量
    public static int currentThread = 0;
    //线程
    public static ArrayList<ServerThread> threadList = new ArrayList<ServerThread>(MAXTHREAD);
    //服务器socket
    private ServerSocket server;
    //数据库参数
    private Connection connection = null;

    /**
     * 这个函数主要来维护服务器为客户端提供的线程.
     * 一旦线程数没有达到最大，就可以把新来的线程加入到列表中.
     * 通过 MAXTHREAD 这个参数来调控最大线程数.
     */
    public ServerDaemon(){
        System.out.println("服务器已经启动");
        //连接数据库
        connectToDB();
        //服务器可视化
        try {
            new ServerVisual(connection.createStatement());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //启动线程
        try {
            server = new ServerSocket(PORT);
            while(true){
                if(currentThread<MAXTHREAD){
                    //假如新入的客户端
                    ServerThread thread = new ServerThread(server.accept(),connection.createStatement());
                    threadList.add(thread);
                    //启动线程
                    thread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                server.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //连接数据库
    public void connectToDB(){
        //数据库信息
        String userName = "root";
        String password = "271828";
        String databaseName = "JavaFinalWork";
        String tableName1 = "AccountTable";
        String tableName2 = "ItemTable";

        try{
            //加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            //建立连接
            String dbURL = "jdbc:mysql://localhost:3306/test?user="+userName+"&password="+password+"&useSSL=true";
            connection = DriverManager.getConnection(dbURL);
            //创建Statement
            Statement statement = connection.createStatement();
            //statement.execute("CREATE DATABASE "+databaseName);
            statement.execute("USE "+databaseName);

            //创建两张表
            //个人信息表
            //statement.execute("CREATE TABLE "+tableName1+"(name char(7),psd char(10),state tinyint)");
            //储户项目表
            //statement.execute("CREATE TABLE "+tableName2+"(name char(7),type char(1),money double,rate float,date Date)");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //入口函数
    public static void main(String[] args) {
        ServerDaemon server = new ServerDaemon();
    }
}

