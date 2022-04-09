package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class ScatterShootStrategy implements Strategy{
    @Override
    public List<BasicBullet> strategyShoot(AbstractAircraft abstractAircraft) {
        List<BasicBullet> res = new LinkedList<>();
        if(abstractAircraft instanceof HeroAircraft){
            int x = abstractAircraft.getLocationX();
            int y = abstractAircraft.getLocationY() + abstractAircraft.getSpeedY();
            int speedX = 0;
            int speedY =abstractAircraft.getSpeedY() -5 ;
            int numOfBullet= abstractAircraft.getNumOfBullet();
            BasicBullet basicBullet;
            int xP=Math.abs(speedY)/numOfBullet;
            for(int i=0; i<numOfBullet; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                speedX=xP*i*2+speedY;
                basicBullet = new HeroBullet(x + (i*2 - numOfBullet + 1)*10, y, speedX, speedY, abstractAircraft.getPower());
                res.add(basicBullet);
            }
        }else{
            int x = abstractAircraft.getLocationX();
            int y = abstractAircraft.getLocationY() + abstractAircraft.getSpeedY();
            int speedX = 0;
            int speedY =abstractAircraft.getSpeedY()+5;
            int numOfBullet= abstractAircraft.getNumOfBullet();
            BasicBullet basicBullet;
            int xP=Math.abs(speedY)/numOfBullet;
            for(int i=0; i<numOfBullet; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                speedX=speedY-xP*i*2;
                basicBullet = new EnemyBullet(x + (i*2 - numOfBullet + 1)*10, y, speedX, speedY, abstractAircraft.getPower());
                res.add(basicBullet);
            }
        }

        return res;
    }
}
