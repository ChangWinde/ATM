package GUI;

import Client.CommunicationClient;
import Client.SingleClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SubWork extends JFrame implements ActionListener{
    private JButton jbConfirm,jbBack;
    private JTextField jtfMoney;
    private JPanel jPanel1,jPanel2,jPanel3;
    private JLabel jlTip;
    private int type,account;
    private String title;
    private CommunicationClient client = SingleClient.getCommunicationClient();
    public SubWork(String title,int type,int account){
        this.type = type;
        this.account = account;
        this.title = title;
        //初始化
        jbConfirm = new JButton("确认");
        jbBack = new JButton("返回");
        jtfMoney = new JTextField(10);
        jlTip = new JLabel("请输入"+title+"金额");
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        //添加部件
        jPanel1.add(jlTip);
        jPanel2.add(jtfMoney);
        jPanel3.add(jbConfirm);
        jPanel3.add(jbBack);
        //添加监视
        jbConfirm.addActionListener(this);
        jbBack.addActionListener(this);
        //界面设置
        this.add(jPanel1);
        this.add(jPanel2);
        this.add(jPanel3);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("中华人民帝国银行");
        this.setLayout(new GridLayout(3,1));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(350, 200, 300, 180);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "确认"){
            if(type == 3){//存钱
                double money = Double.parseDouble(this.jtfMoney.getText());
                String tag = client.save(money,account);
                //重置输入栏
                this.jtfMoney.setText("");
                if(tag.equals("Y")) {
                    JOptionPane.showMessageDialog(null, "存储成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }else if(tag.equals("N")){
                    JOptionPane.showMessageDialog(null, "存储失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }else if(tag.equals("")){
                    JOptionPane.showMessageDialog(null, "请输入合法值！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }
            }else if(type == 4){//取钱
                double money = Double.parseDouble(this.jtfMoney.getText());
                String tag = client.withdraw(money,account);
                //重置输入栏
                this.jtfMoney.setText("");
                if(tag.equals("Y")) {
                    JOptionPane.showMessageDialog(null, "取款成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }else if(tag.equals("N")){
                    JOptionPane.showMessageDialog(null, "余额不足！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }else if(tag.equals("")){
                    JOptionPane.showMessageDialog(null, "请输入合法值！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }
            }else if(type ==6){//贷款
                double money = Double.parseDouble(this.jtfMoney.getText());
                String tag = client.loan(money);
                //重置输入栏
                this.jtfMoney.setText("");
                if(tag.equals("Y")) {
                    JOptionPane.showMessageDialog(null, "贷款成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }else if(tag.equals("N1")){
                    JOptionPane.showMessageDialog(null, "您不是活跃用户！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }else if(tag.equals("N2")){
                    JOptionPane.showMessageDialog(null, "贷款超过额度！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }else if(tag.equals("N3")){
                    JOptionPane.showMessageDialog(null, "请先将贷款还清！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }else if(tag.equals("")){
                    JOptionPane.showMessageDialog(null, "请输入合法值！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }
            }else if(type ==7){//还款
                double money = Double.parseDouble(this.jtfMoney.getText());
                String tag = client.pay(money);
                //重置输入栏
                this.jtfMoney.setText("");
                //响应客户
                if(tag.equals("Y1")) {
                    JOptionPane.showMessageDialog(null, "还款成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }else if (tag.equals("Y2")) {
                    JOptionPane.showMessageDialog(null, "贷款还清！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }else if(tag.equals("N")){
                    JOptionPane.showMessageDialog(null, "还款数目不足！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }else if(tag.equals("")){
                    JOptionPane.showMessageDialog(null, "请输入合法值！", "提示消息", JOptionPane.WARNING_MESSAGE);
                }
            }
        }else if(e.getActionCommand() == "返回"){
            this.dispose();
            if(account == 3)
                new LoanAndPay();
            else
                new Work(title,account);
        }
    }
}
