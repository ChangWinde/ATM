package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoanAndPay extends JFrame implements ActionListener {
    private JButton jbLoan,jbPay,jbBack;
    private JPanel jPanel;

    public LoanAndPay(){
        //部件初始化
        jbLoan = new JButton("贷款");
        jbPay = new JButton("还款");
        jbBack = new JButton("退出");
        jPanel = new JPanel();
        //面板添加元素
        jPanel.add(jbLoan);
        jPanel.add(jbPay);
        jPanel.add(jbBack);
        //添加监视
        jbLoan.addActionListener(this);
        jbPay.addActionListener(this);
        jbBack.addActionListener(this);
        //界面射界
        jPanel.setLayout(new GridLayout(3,1));
        this.add(jPanel);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("中华人民帝国银行");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(350, 200, 300, 180);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "贷款") {
            this.dispose();
            new SubWork("贷款",6,3);
        }else if(e.getActionCommand() == "还款") {
            this.dispose();
            new SubWork("还款",7,3);
        }else if(e.getActionCommand() == "退出"){
            this.dispose();
            new SelectModel();
        }
    }

    public static void main(String[] args) {
        LoanAndPay loanAndPay = new LoanAndPay();
    }
}
