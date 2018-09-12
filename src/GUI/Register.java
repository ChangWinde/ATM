package GUI;

import Client.CommunicationClient;
import Client.SingleClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;

public class Register extends JFrame implements ActionListener{
    private JPanel jPanel;
    private JLabel jlName,jlPassword,jlPsdConfirm;
    private JTextField jtfName;
    private JButton jbReturn,jbRegister;
    private JPasswordField jPasswordField,jPsdFieldConfirm;
    //通信客户端
    private CommunicationClient client = SingleClient.getCommunicationClient();
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "注册") {
            //响应注册
            if(!this.jPsdFieldConfirm.getText().equals(this.jPasswordField.getText())) {
                JOptionPane.showMessageDialog(null, "两次密码不一致！", "提示消息", JOptionPane.WARNING_MESSAGE);
                //重置字段
                this.jPsdFieldConfirm.setText("");
                this.jPasswordField.setText("");
                //退出
                return;
            }else{
                String tag = client.register(jtfName.getText(),jPasswordField.getText());
                if(tag.equals("Y")){
                    JOptionPane.showMessageDialog(null, "注册成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
                    //隐藏界面
                    this.dispose();
                    //生成下一个界面
                    new SelectModel();
                }else{
                    JOptionPane.showMessageDialog(null, "该账户名已经被使用！", "提示消息", JOptionPane.WARNING_MESSAGE);
                    //重置字段
                    this.jtfName.setText("");
                    this.jPsdFieldConfirm.setText("");
                    this.jPasswordField.setText("");
                }
            }
        }else if(e.getActionCommand() == "返回") {
            //响应返回
            this.dispose();
            new Login();
        }
    }
    //注册界面
    public Register(){
        //初始化组件
        jPanel=new JPanel();

        jlName = new JLabel("请输入用户名: ");
        jtfName = new JTextField(10);
        jtfName.setToolTipText("用户名必须为3-6位字母或者数字");

        jlPassword = new JLabel("请输入密 码: ");
        jPasswordField = new JPasswordField(10);
        jPasswordField.setToolTipText("密码必须为6位数字");

        jlPsdConfirm = new JLabel("请确认密 码: ");
        jPsdFieldConfirm = new JPasswordField(10);

        jbReturn = new JButton("返回");
        jbReturn.setToolTipText("点击，返回登录界面");
        jbRegister = new JButton("注册");

        //添加监视
        jbReturn.addActionListener(this);
        jbRegister.addActionListener(this);

        jPanel.setLayout(new GridLayout(4,2));

        jPanel.add(jlName);
        jPanel.add(jtfName);

        jPanel.add(jlPassword);
        jPanel.add(jPasswordField);

        jPanel.add(jlPsdConfirm);
        jPanel.add(jPsdFieldConfirm);

        jPanel.add(jbReturn);
        jPanel.add(jbRegister);

        //设置界面
        this.add(jPanel);
        this.setTitle("注册");
        this.setBounds(200, 100, 400, 150);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //注册函数
    public void register(){
        //这个地方需要将数据加入数据库
        if(!this.jPsdFieldConfirm.getText().equals(this.jPasswordField.getText())) {
            JOptionPane.showMessageDialog(null, "两次密码不一致！", "提示消息", JOptionPane.WARNING_MESSAGE);
            //重置字段
            this.jPsdFieldConfirm.setText("");
            this.jPasswordField.setText("");
            //退出
            return;
        }
        //验证账户名是否被使用过
        client.out.println("1&"+this.jtfName.getText()+"&"+this.jPasswordField.getText());
        //接受返回信息
        String tag = null;
        try {
            tag = client.in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //验证消息处理阶段
        if(tag.equals("Y")) {
            JOptionPane.showMessageDialog(null, "注册成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
            //隐藏界面
            this.dispose();
            //生成下一个界面
            new SelectModel();
        }else{
            JOptionPane.showMessageDialog(null, "该账户名已经被使用！", "提示消息", JOptionPane.WARNING_MESSAGE);
            //重置字段
            this.jtfName.setText("");
            this.jPsdFieldConfirm.setText("");
            this.jPasswordField.setText("");
        }
    }
}
