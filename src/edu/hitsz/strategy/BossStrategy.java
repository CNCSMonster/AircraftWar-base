package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class BossStrategy implements Strategy {


    @Override
    public List<BasicBullet> strategyShoot(AbstractAircraft abstractAircraft) {
        //boss机射击是散射和直射
        List<BasicBullet> res = new LinkedList<>();
        int x = abstractAircraft.getLocationX();
        int y = abstractAircraft.getLocationY() +2;
        BasicBullet basicBullet;
        int speedX=0;
        int speedY=5;
        int numOfBullet=5;  //子弹数量
        int xPer=Math.abs(speedY)/numOfBullet;
        //散射子弹
        for(int i=0; i<numOfBullet; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            speedX=xPer*2*i-speedY;
            basicBullet = new EnemyBullet(x + (i*2-5)*10, y, speedX, speedY, 30);
            res.add(basicBullet);
        }
        return res;
    }

}
