package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.ScatterShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class BossEnemy extends AbstractAircraft{


    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.power=100;
        this.numOfBullet=5;
        this.setShootStrategy(new ScatterShootStrategy());
    }

    // 重载一个速度、血量自定义的构造方法
    public BossEnemy(int locationX, int locationY) {
        super(locationX,locationY,40,0,250);
        this.power=100;
        this.numOfBullet=5;
        this.setShootStrategy(new ScatterShootStrategy());
    }
    public BossEnemy(int addBlood){
        super((int) ( Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth()))*1,
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,40,0,250+addBlood);
        this.power=100;
        this.numOfBullet=5;
        this.setShootStrategy(new ScatterShootStrategy());
    }


    @Override
    public void forward() {
        //Boss机的飞行,只能够缓慢左右移动

        //1/50机会瞬移，其他情况不动
        int r=(int)(Math.random()*4);
        if(r==1){
            //随机往左或者右瞬移
            //得到1或者-1
            int lOrR=2*(int)(Math.random()*2)-1;
            //瞬移距离为1/3窗口左右
            int dis=Main.WINDOW_WIDTH/3;
            if(locationX+dis*lOrR<0||locationX+dis*lOrR>Main.WINDOW_WIDTH){
                ;
            }else{
                locationX+=lOrR*dis;
            }
        }




        //Boss机不能出界


    }


    //使用观察者模式的观察方法,获得道具的影响
    @Override
    public void getEffected(AbstractProp abstractProp){
        this.vanish();
    }


}
