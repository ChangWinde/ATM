package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

public class ServerVisual extends JFrame implements ActionListener{
    private Statement statement;
    private JLabel jluser1,jluser2,jlMoney1,jlMoney2,jlLoan1,jlLoan2;
    private JPanel jPanel1,jPanel2,jPanel3;
    public ServerVisual(Statement statement){
        this.statement = statement;
        jluser1 = new JLabel("银行总用户数量");
        jlMoney1 = new JLabel("银行总存款");
        jlLoan1 = new JLabel("银行总贷款");
        jluser2 = new JLabel();
        jlMoney2 = new JLabel();
        jlLoan2 = new JLabel();
        //面板
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        //添加部件
        jPanel1.add(jluser1);
        jPanel1.add(jluser2);
        jPanel2.add(jlMoney1);
        jPanel2.add(jlMoney2);
        jPanel3.add(jlLoan1);
        jPanel3.add(jlLoan2);
        //界面设计
        this.add(jPanel1);
        this.add(jPanel2);
        this.add(jPanel3);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("中华人民帝国银行");
        this.setLayout(new GridLayout(3,1));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(350, 200, 300, 180);

        jluser2.setText(userCount());
        jlMoney2.setText(moneyCount());
        jlLoan2.setText(loanCount());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    //用户数量统计
    public String userCount(){
        try {
            int count = 0;
            ResultSet resultSet = statement.executeQuery("SELECT * FROM AccountTable");
            while(resultSet.next())
                count++;
            return count+"";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    //存款金额统计
    public String moneyCount(){
        //计算金额
        double money = 0.0;
        ResultSet resultSet1 = null;
        try {
            resultSet1 = statement.executeQuery("SELECT money FROM ItemTable WHERE type IN (1,2)");
            while(resultSet1.next()){
                money += resultSet1.getDouble("money");
            }
            //截取小数
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(money);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    //贷款金额统计
    public String loanCount(){
        //计算金额
        double money = 0.0;
        ResultSet resultSet1 = null;
        try {
            resultSet1 = statement.executeQuery("SELECT money FROM ItemTable WHERE type = 3");
            while(resultSet1.next()){
                money += resultSet1.getDouble("money");
            }
            //截取小数
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(money);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
