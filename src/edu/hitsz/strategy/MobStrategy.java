package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class MobStrategy implements Strategy{



    @Override
    public List<BasicBullet> strategyShoot(AbstractAircraft abstractAircraft) {
        List<BasicBullet> res = new LinkedList<>();
        int x = abstractAircraft.getLocationX();
        int y = abstractAircraft.getLocationY() +2+abstractAircraft.getSpeedY();
        int speedX = 0;
        int speedY = 5;
        BasicBullet basicBullet;
        for(int i=0; i<1; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            basicBullet = new EnemyBullet(x + (i*2-1)*10, y, speedX, speedY, 30);
            res.add(basicBullet);
        }
        return res;
    }
}
