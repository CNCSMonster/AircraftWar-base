package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.strategy.ScatterShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class BossEnemy extends AbstractAircraft{


    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    // 重载一个速度、血量自定义的构造方法
    public BossEnemy(int locationX, int locationY) {
        super(locationX,locationY,40,0,250);
        this.power=100;
        this.numOfBullet=5;
        this.setShootStrategy(new ScatterShootStrategy());
    }


    @Override
    public void forward() {
        //Boss机的飞行,只能够缓慢左右移动

        //1/50机会瞬移，其他情况不动
        int r=(int)(Math.random()*100);
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
//
//    @Override
//    public List<BasicBullet> shoot() {
//        List<BasicBullet> res = new LinkedList<>();
//        int x = this.getLocationX();
//        int y = this.getLocationY() + direction*2;
//        int speedX = 0;
//        int speedY = this.getSpeedY() + direction*5;
//        BasicBullet basicBullet;
//        for(int i=0; i<shootNum; i++){
//            // 子弹发射位置相对飞机位置向前偏移
//            // 多个子弹横向分散
//            basicBullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
//            res.add(basicBullet);
//        }
//        return res;
//    }

}
