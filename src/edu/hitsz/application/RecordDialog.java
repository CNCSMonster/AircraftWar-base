package edu.hitsz.application;

import edu.hitsz.dao.Record;
import edu.hitsz.dao.RecordDAO;
import edu.hitsz.dao.RecordDAOImpl;

import javax.management.StringValueExp;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static edu.hitsz.application.Main.WINDOW_HEIGHT;
import static edu.hitsz.application.Main.WINDOW_WIDTH;

public class RecordDialog extends JDialog {


    public RecordDialog(int score) throws IOException {
        setTitle("排行榜界面");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //设置窗口的大小和位置,居中放置
        setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        setVisible(true);
        Container container=getContentPane();
        container.setLayout(new BorderLayout());

        RecordDAO recordDAO=new RecordDAOImpl();

        //记录获取窗口
        String name=JOptionPane.showInputDialog(this,"请输入玩家名：","添加记录窗口",JOptionPane.PLAIN_MESSAGE);

        if(name!=null){
            recordDAO.doAdd(new Record(score,name));
        }
        List<Record> allList=recordDAO.getAllRecords();

        String[] colNames={"排名","玩家名","分数","时间"};
        String[][] data=new String[allList.size()][4];
        for(int i=0;i<allList.size();i++){
            data[i][0]= String.valueOf(i+1);
            Record record=allList.get(i);
            data[i][1]=record.getName();
            data[i][2]=String.valueOf(record.getScore());
            data[i][3]=record.getDate().toString();
        }

        //使用内部类设置单元格不可修改
        DefaultTableModel mode=new DefaultTableModel(data, colNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable jTable=new JTable(mode);
        container.add(BorderLayout.CENTER,new JScrollPane(jTable));
        setVisible(true);

        JButton jButton=new JButton("删除选中行");
        jButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows=jTable.getSelectedRows();
                for(int i:rows){
                    System.out.println(i);
                    try {
                        recordDAO.doDelete(allList.get(i));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    mode.removeRow(i);
                }
                for(int i=0;i<mode.getColumnCount();i++){
                    mode.setValueAt(i+1,i,0);
                }
            }
        });
        JPanel jPanel=new JPanel();
        jPanel.add(jButton);
        container.add(BorderLayout.NORTH,jPanel);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new RecordDialog(22);
    }


}
