package edu.hitsz.application;

import edu.hitsz.dao.Record;
import edu.hitsz.dao.RecordDAOImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static edu.hitsz.application.Main.WINDOW_HEIGHT;
import static edu.hitsz.application.Main.WINDOW_WIDTH;

class RecordIfAdd extends  JDialog{
    public RecordIfAdd(int score){
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //外部应该采用绝对布局
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        setLayout(null);
        JLabel jLabel=new JLabel("输入游戏名：");
        jLabel.setBounds(50,50,100,50);
        JTextField jTextField=new JTextField();
        jTextField.setBounds(150,50,100,50);
        JButton jButton=new JButton("确认加入记录");
        jButton.setBounds(100,100,100,50);
        Container container=getContentPane();
        container.add(jLabel);
        container.add(jTextField);
        container.add(jButton);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //加入新记录
                String name=jTextField.getText();
                Record record=new Record(score,name);
                RecordDAOImpl recordDAO=new RecordDAOImpl();
                try {
                    recordDAO.doAdd(record);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                setVisible(false);
            }
        });

    }
}


public class RecordDialog extends JDialog {


    public RecordDialog(int score){
        setTitle("排行榜界面");
        //是否开启背景音乐
        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //设置窗口的大小和位置,居中放置
        setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        setVisible(true);
        Container container=getContentPane();

        //把子窗口加入总界面


        new RecordIfAdd(score);

        RecordDAOImpl recordDAO=new RecordDAOImpl();

        List<Record> allList=new ArrayList<>();
        try {
            allList=recordDAO.getAllRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //一个滚动列表
        String[] columnNames={"排名","玩家名","得分"};
        String[][] tableValues=new String[allList.size()][3];
        //完成表格数据的初始化
        for(int i=0;i<allList.size();i++){
            tableValues[i][0]=String.valueOf(i+1);
            tableValues[i][1]=allList.get(i).getName();
            tableValues[i][2]=String.valueOf(allList.get(i).getScore());
        }
        JTable jTable=new JTable(tableValues,columnNames);
        jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        //根据table生成表格的滚动面板
        JScrollPane jScrollPane=new JScrollPane(jTable);
        jScrollPane.setBounds(50,50,300,300);


        //添加按钮，用来删除记录,
        //按钮不能直接在JFrame上显示，需要有一个Panel
        JPanel buttonPanel=new JPanel();

        //使用五方布局管理器
        setLayout(new BorderLayout());
        container.add(jScrollPane,BorderLayout.CENTER);
        container.add(buttonPanel,BorderLayout.SOUTH);

        buttonPanel.setBounds(50,400,300,100);
        JButton jButton=new JButton("删除");
        jButton.setBounds(0,0,100,50);
        buttonPanel.add(jButton);
        List<Record> finalAllList = allList;
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] curRows=jTable.getSelectedRows();
                for(int i=curRows.length-1;i>=0;i--){
                    //从总列表里面删去这个记录
                    //也从实际上表格里面删去这个记录
//                    Record curRecord= finalAllList.get(curRows[i]);
//                    try {
//                        recordDAO.doDelete(curRecord);
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
                    jTable.remove(curRows[i]);
                }

            }
        });

    }

    public static void main(String[] args) {
        new RecordDialog(22);
    }


}
