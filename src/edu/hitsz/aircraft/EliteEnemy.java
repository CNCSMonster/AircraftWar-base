package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends AbstractAircraft{
    private int shootNum = 1;     //子弹一次发射数量
    private int power = 50;       //精英敌机子弹伤害
    private int direction = 1;  //子弹射击方向 (向上发射：1，向下发射：-1)

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    // 重载一个速度、血量自定义的构造方法
    public EliteEnemy(int locationX, int locationY) {
        super(locationX,locationY,40,10,50);
    }


    @Override
    public void forward() {
        // 重写父类的移动方法
        int a=(int)(Math.random()*10);
        if(a<5){
            //一半的几率向下移动
            this.locationY+=speedY;
        }else if(a>=5&&a<7){
            //五分之一的几率左右移动
            a= (int) (Math.random()*2);
            if(a==0){   //向左
                if(this.locationX-speedX>0){
                    this.locationX-=speedX;
                }
            }else{  //向右
                if(this.locationX+speedX<Main.WINDOW_WIDTH){
                    this.locationX+=speedX;
                }
            }
        }else{
            //还有3/10的几率不动
        }
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<BasicBullet> shoot() {
        List<BasicBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction*5;
        BasicBullet basicBullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            basicBullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            res.add(basicBullet);
        }
        return res;
    }
}
