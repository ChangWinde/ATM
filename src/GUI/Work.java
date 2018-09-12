package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Work extends JFrame implements ActionListener{
    private String name;
    private int account;
    private JPanel jPanel;
    private JButton jbQuery,jbSave,jbWithdraw,jbBack;

    public Work(String name,int account){
        this.account = account;
        this.name = name;
        //部件初始化
        jPanel = new JPanel();
        jbQuery = new JButton("查询余额");
        jbSave = new JButton("存钱");
        jbWithdraw = new JButton("取钱");
        jbBack = new JButton("返回");
        //为面板添加部件
        jPanel.add(jbQuery);
        jPanel.add(jbSave);
        jPanel.add(jbWithdraw);
        jPanel.add(jbBack);
        //设置监视
        jbQuery.addActionListener(this);
        jbSave.addActionListener(this);
        jbWithdraw.addActionListener(this);
        jbBack.addActionListener(this);
        //界面设计
        jPanel.setLayout(new GridLayout(4,1));
        this.add(jPanel);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle(name);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(300, 200, 400, 180);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "查询余额"){
            new MoneyQuery(name,account);
        }else if(e.getActionCommand() == "取钱"){
            this.dispose();
            new SubWork("取款",4,account);
        }else if(e.getActionCommand() == "存钱"){
            this.dispose();
            new SubWork("存款",3,account);
        }else if(e.getActionCommand() == "返回"){
            this.dispose();
            new SelectModel();
        }
    }

}
