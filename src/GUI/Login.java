package GUI;

import Client.CommunicationClient;
import Client.SingleClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Login extends JFrame implements ActionListener {
    private JButton jbLogin,jbRegister,jbExit;
    private JLabel jlName,jlPassword;
    private JTextField jtfName;
    private JPanel jPanel1,jPanel2,jPanel3;
    private JPasswordField jPasswordField;
    //客户端
    private static CommunicationClient client = SingleClient.getCommunicationClient();
    //登录界面的生成
    public Login(){
        //创建Button
        jbLogin = new JButton("登录");
        jbRegister = new JButton("注册");
        jbExit = new JButton("退出");

        //设置Label
        jlName = new JLabel("用户名：");
        //jlName.setFont(new Font("楷体",Font.PLAIN,15));
        jlPassword = new JLabel("密 码： ");

        //设置JTextField
        jtfName = new JTextField(10);

        //设置JPasswordField
        jPasswordField = new JPasswordField(10);

        //设置监听
        jbLogin.addActionListener(this);
        jbRegister.addActionListener(this);
        jbExit.addActionListener(this);

        //设置面板
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        //为三个面板添加元素
        jPanel1.add(jlName);
        jPanel1.add(jtfName,BorderLayout.CENTER);

        jPanel2.add(jlPassword);
        jPanel2.add(jPasswordField,BorderLayout.CENTER);

        jPanel3.add(jbLogin);
        jPanel3.add(jbRegister);
        jPanel3.add(jbExit);

        //加到窗体中
        this.add(jPanel1);
        this.add(jPanel2);
        this.add(jPanel3);
        //界面设置
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("中华人民帝国银行");
        this.setLayout(new GridLayout(3,1));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(350, 200, 300, 180);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "退出") {
            System.exit(0);
        }else if(e.getActionCommand() == "登录") {
            //登录
            String tag = client.login(jtfName.getText(),jPasswordField.getText());
            if(tag.equals("Y")){
                //隐藏界面
                this.dispose();
                //生成业务界面
                new SelectModel();
            }else{
                //登陆失败
                JOptionPane.showMessageDialog(null, "登录失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
                //重置输入栏
                this.jtfName.setText("");
                this.jPasswordField.setText("");
            }
        }else if(e.getActionCommand() == "注册"){
            //注册方法
            this.register();
        }
    }
    //注册跳转
    public void register(){
        //隐层该界面
        this.dispose();
        //生成注册界面
        new Register();
    }
    //登录响应
    public void login(){
        //客户端
        client = SingleClient.getCommunicationClient();
        client.setName(this.jtfName.getText());

        String tag = null;
        client.out.println("0&"+jtfName.getText()+"&"+jPasswordField.getText());
        try {
            tag = client.in.readLine();
            if(tag.equals("Y")){
                //登录成功
                //隐藏界面
                this.dispose();
                //生成业务界面
                new SelectModel();
            }else
            {
                //登陆失败
                JOptionPane.showMessageDialog(null, "登录失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
                //重置输入栏
                this.jtfName.setText("");
                this.jPasswordField.setText("");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Login login = new Login();

    }
}
