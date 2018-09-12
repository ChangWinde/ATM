package GUI;

import Client.SingleClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SavingOpen extends JFrame implements ActionListener{
    private JButton jbConfirm;
    private JLabel jlMoney,jlTip;
    private JTextField jTextField;
    private JPanel jPanel1,jPanel2,jPanel3,jPanel4;
    private JRadioButton jRadioButton1,jRadioButton2,jRadioButton3;
    private ButtonGroup buttonGroup;
    public SavingOpen(){
        //设置面板
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        jPanel4 = new JPanel();
        //初始化部件
        jlTip = new JLabel("请选择定期年限:");
        jlMoney = new JLabel("请输入金额:");

        jRadioButton1 = new JRadioButton("半年期[2.0%]");
        jRadioButton2 = new JRadioButton("一年期[3.0%]");
        jRadioButton3 = new JRadioButton("五年期[4.5%]");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);

        jTextField = new JTextField(10);
        jbConfirm = new JButton("确认");
        //为面板添加部件
        jPanel1.add(jlTip);
        jPanel2.add(jRadioButton1);
        jPanel2.add(jRadioButton2);
        jPanel2.add(jRadioButton3);
        jPanel3.add(jlMoney);
        jPanel3.add(jTextField);
        jPanel4.add(jbConfirm);
        //设置面板
//        jPanel1.setLayout(new GridLayout(1,1));
//        jPanel1.setLayout(new GridLayout(1,3));
//        jPanel1.setLayout(new GridLayout(1,2));
//        jPanel1.setLayout(new GridLayout(1,1));
        //设置监视
        jbConfirm.addActionListener(this);
        //界面设置
        this.add(jPanel1);
        this.add(jPanel2);
        this.add(jPanel3);
        this.add(jPanel4);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("开通储蓄项目");
        this.setLayout(new GridLayout(4,1));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(300, 200, 400, 200);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "确认"){
            if(this.jTextField.getText().equals("")){
                JOptionPane.showMessageDialog(null, "信息不全，无法开项目！", "提示消息", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(Double.parseDouble(this.jTextField.getText())<0){
                JOptionPane.showMessageDialog(null, "输入信息有误！", "提示消息", JOptionPane.WARNING_MESSAGE);
                return;
            }
            //上传信息
            update();
            //隐藏界面
            this.dispose();
            //提示信息
            JOptionPane.showMessageDialog(null, "创建成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
            new Work("储蓄账户",1);
        }
    }

    //上传开户信息
    public void update(){
        //设置时间格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        //设置户名
        String name = SingleClient.getCommunicationClient().getName();
        //利息
        double rate;
        //设置利息
        if(jRadioButton1.isSelected()){
            rate = 0.02;
        }else if(jRadioButton2.isSelected()){
            rate = 0.03;
        }else{
            rate = 0.045;
        }
        //获取金额
        double money = Double.parseDouble(this.jTextField.getText());
        //时间
        java.util.Date d1 = new Date();
        //上传数据库
        SingleClient.getCommunicationClient().out.println("8#"+name+"&"+"1&"+money+"&"+rate+"&"+d1.getTime());
    }
}
