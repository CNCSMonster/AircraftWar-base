package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static edu.hitsz.application.Main.WINDOW_HEIGHT;
import static edu.hitsz.application.Main.WINDOW_WIDTH;

/**
 * 程序入口
 * @author hitsz
 */
class MusicChoose extends  JDialog{
    //是否开启背景音乐
    private static boolean ifUseBgm=true;

    public  static boolean getIfUseBgm(){
        return ifUseBgm;
    }

    //音效
    public MusicChoose(){
        this.setTitle("音效选择界面");
        //是否开启背景音乐
        setVisible(true);
        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        //设置窗口的大小和位置,居中放置
        setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        Container container=getContentPane();
        //加入一个独选框
        //加入一个选择
        setLayout(null);
        JLabel jLabel=new JLabel("是否开启背景音乐");
        jLabel.setBounds(150,150,200,50);
        container.add(jLabel);


        //开启单选框
        JRadioButton jRadioButton=new JRadioButton("是");
        JRadioButton jRadioButton2=new JRadioButton("否");
        jRadioButton.setBounds(150,200,50,50);
        jRadioButton2.setBounds(200,200,50,50);
        ButtonGroup group=new ButtonGroup();
        group.add(jRadioButton);
        group.add(jRadioButton2);
        container.add(jRadioButton);
        container.add(jRadioButton2);

        jRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ifUseBgm=true;
            }
        });

        jRadioButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ifUseBgm=false;
            }
        });
    }
}

//难度选择界面
class LevelChoose extends JDialog{

    public LevelChoose(){
        setVisible(true);
        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        //设置窗口的大小和位置,居中放置
        setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);


        //使用绝对布局管理，需要设置布局管理器为null
        setLayout(null);
        Container container=getContentPane();


        JLabel jLabel=new JLabel("当前难度："+Game.getLevel());
        jLabel.setBounds(150,50,200,40);
        container.add(jLabel);

        //加入按钮
        JButton jButton=new JButton("简单");
        JButton jButton1=new JButton("中等");
        JButton jButton2=new JButton("困难");
        jButton.setBounds(150,150,200,40);
        jButton1.setBounds(150,250,200,40);
        jButton2.setBounds(150,350,200,40);
        container.add(jButton);
        container.add(jButton1);
        container.add(jButton2);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.setLevel(0);
                jLabel.setText("当前难度:"+Game.getLevel());
            }
        });
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.setLevel(1);
                jLabel.setText("当前难度:"+Game.getLevel());
            }
        });
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.setLevel(2);
                jLabel.setText("当前难度:"+Game.getLevel());
            }
        });
    }
}
//开始游戏界面
class StartGame extends JDialog{
    public StartGame(){
        setVisible(true);
        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        //设置窗口的大小和位置,居中放置
        setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        Container container=getContentPane();
        Game game=new Game();
        add(game);
        game.action();
    }
}



class Menu extends JFrame{
    public Menu(String name){
        setVisible(true);
        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        //设置窗口的大小和位置,居中放置
        setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //使用绝对布局管理，需要设置布局管理器为null
        setLayout(null);
        Container container=getContentPane();

        //加入按钮
        JButton jButton=new JButton("难度选择");
        JButton jButton1=new JButton("开始游戏");
        JButton jButton2=new JButton("音效设置");
        jButton.setBounds(150,150,200,40);
        jButton1.setBounds(150,250,200,40);
        jButton2.setBounds(150,350,200,40);
        container.add(jButton);
        container.add(jButton1);
        container.add(jButton2);
        //实现三个选项，难度选择，开始游戏，以及音效设置
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LevelChoose();
            }
        });
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StartGame();
            }
        });
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MusicChoose();
            }
        });

    }
}




public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static void main(String[] args) {
        new Menu("Aircraft War Menu");

    }
}
