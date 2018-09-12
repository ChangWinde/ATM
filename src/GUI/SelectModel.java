package GUI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectModel extends JFrame implements ActionListener{
    private JButton jbSaving,jbCheck,jbloan,jbExit;
    private JPanel jPanel;
    public SelectModel(){
        jbSaving = new JButton("储蓄账户");
        jbCheck = new JButton("支票账户");
        jbloan = new JButton("贷款");
        jbExit = new JButton("退出");
        //平面
        jPanel = new JPanel();

        jPanel.add(jbSaving);
        jPanel.add(jbCheck);
        jPanel.add(jbloan);
        //jPanel.add(jbExit);
        jPanel.setLayout(new GridLayout(3,1));
        //设置监视
        jbSaving.addActionListener(this);
        jbCheck.addActionListener(this);
        jbloan.addActionListener(this);
        jbExit.addActionListener(this);
        //界面设计
        this.add(jPanel);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("账户类型选择");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(300, 300, 300, 180);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "储蓄账户"){
            this.dispose();
            new SavingSelect();
        }else if(e.getActionCommand() == "支票账户"){
            this.dispose();
            //支票账户
            new Work("支票账户",2);
        }else if(e.getActionCommand() == "贷款"){
            this.dispose();
            //贷款
            new LoanAndPay();
        }
    }

    public static void main(String[] args) {
        SelectModel selectModel = new SelectModel();
    }
}
