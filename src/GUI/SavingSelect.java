package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SavingSelect extends JFrame implements ActionListener{
    private JButton jbOpenNew,jbOpenOld,jbBack;
    private JPanel jPanel;
    public SavingSelect(){
        //初始化部件
        jbOpenNew = new JButton("创建新账户");
        jbOpenOld = new JButton("查看已有账户");
        jbBack = new JButton("返回");
        jPanel = new JPanel();
        //为面板添加部件
        jPanel.add(jbOpenNew);
        jPanel.add(jbOpenOld);
        jPanel.add(jbBack);
        //添加监视
        jbOpenNew.addActionListener(this);
        jbOpenOld.addActionListener(this);
        jbBack.addActionListener(this);
        //界面设置
        jPanel.setLayout(new GridLayout(3,1));
        this.add(jPanel);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("储蓄账户");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(300, 200, 300, 180);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "创建新账户"){
            this.dispose();
            //创建新账户界面
            new SavingOpen();
        }else if(e.getActionCommand() == "查看已有账户"){
            this.dispose();
            new Work("储蓄账户",1);
        }else if(e.getActionCommand() == "返回"){
            this.dispose();
            new SelectModel();
        }
    }
}
