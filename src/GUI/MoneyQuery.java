package GUI;

import Client.CommunicationClient;
import Client.SingleClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MoneyQuery extends JFrame implements ActionListener{
    private String title;
    private int account;
    private JLabel jlTip,jlContent;
    private JPanel jPanel1,jPanel2,jPanel3;
    private JButton jbBack;
    private CommunicationClient client = SingleClient.getCommunicationClient();

    public MoneyQuery(String title,int account){
        this.title = title;
        this.account = account;
        //初始化部件
        jlTip = new JLabel("你的"+title+"账户余额是：");
        jlContent = new JLabel("");
        jbBack = new JButton("返回");

        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        //添加部件
        jPanel1.add(jlTip);
        jPanel2.add(jlContent);
        jPanel3.add(jbBack);
        //添加监视
        jbBack.addActionListener(this);
        //界面设置
        this.add(jPanel1);
        this.add(jPanel2);
        this.add(jPanel3);
        this.setLayout(new GridLayout(3,1));
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("余额查询");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(350, 200, 300, 180);

        String message = client.query(account);
        this.jlContent.setText(message);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "返回"){
            this.dispose();
        }
    }

}
