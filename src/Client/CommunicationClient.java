package Client;

import GUI.SelectModel;
import Server.ServerDaemon;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class CommunicationClient {
    private Socket client = null;
    //输入输出流
    public static BufferedReader in = null;
    public static PrintWriter out = null;
    //用户名
    public static String name;

    public CommunicationClient(){
        try{
            //连接服务器
            client = new Socket(InetAddress.getLocalHost(), ServerDaemon.PORT);
            //初始化流
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setName(String name){this.name = name;}

    public String getName(){return name;}
    //登录
    public static String login(String name,String psd){
        CommunicationClient.name = name;
        //上传数据
        String tag = null;
        out.println("0&"+name+"&"+psd);
        try {
            tag = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tag;
    }
    //注册
    public static String register(String name,String psd){
        //验证账户名是否被使用过
        out.println("1&"+name+"&"+psd);
        //接受返回信息
        String tag = null;
        try {
            tag = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tag;
    }
    //查询余额
    public static String query(int account) {
        //给服务器发消息
        out.println("5#" + name+ "&" + account + "&&&");
        //接受回复
        String message = null;
        try {
            message = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //响应客户
        return message;
    }
    //存钱
    public static String save(double money,int account){
        if(money <= 0){
            return "";
        }
        double rate = 0.0;
        //类型甄别
        if(account == 2){
            rate = 0.0015;
        }
        //上传服务器
        out.println("3#"+name+"&"+account+"&"+money+"&"+rate+"&");
        //回传消息
        String tag = null;
        try {
            tag = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tag;
    }
    //取钱
    public static String withdraw(double money,int account){
        //判断输入的合法性
        if(money <= 0){
            return "";
        }
        out.println("4#"+name+"&"+account+"&"+money+"&&");
        //处理返回值
        String tag = null;
        try {
            tag = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tag;
    }
    //贷款
    public static String loan(double money){
        //判断输入的合法性
        if(money <= 0){
            return "";
        }
        //贷款利息
        double rate = 0.05;
        Date date = new Date();
        out.println("6#"+name+"&&"+money+"&"+rate+"&"+date.getTime());
        //处理返回值
        String tag = null;
        try {
            tag = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tag;
    }
    //还款
    public static String pay(double money){
        //判断输入的合法性
        if(money <= 0){
            return "";
        }
        //上传消息
        out.println("7#"+name+"&3&"+money+"&&");
        //响应客户端
        String tag = null;
        try {
            tag = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tag;
    }

    public static void main(String[] args) {
        CommunicationClient communicationClient = new CommunicationClient();
        communicationClient.out.println("你好");
    }
}
