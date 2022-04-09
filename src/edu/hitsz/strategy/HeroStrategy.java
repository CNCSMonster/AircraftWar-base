package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class HeroStrategy implements Strategy{



    @Override
    public List<BasicBullet> strategyShoot(AbstractAircraft abstractAircraft) {
        var ifon=((HeroAircraft)abstractAircraft).getIfon();
        List<BasicBullet> res = new LinkedList<>();
        int x = abstractAircraft.getLocationX();
        int y = abstractAircraft.getLocationY() -2;
        int speedX = 0;
        int speedY = -5;
        int numOfBullet=0;  //子弹数量
        BasicBullet basicBullet;
        if(ifon==0){
            for(int i=0; i<1; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                basicBullet = new HeroBullet(x + (i*2)*10, y, speedX, speedY, 30);
                res.add(basicBullet);
            }
        }else if(ifon==1){
            numOfBullet=5;
            for(int i=0; i<numOfBullet; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                basicBullet = new HeroBullet(x + (i*2-5)*10, y, speedX, speedY, 30);
                res.add(basicBullet);
            }
        }else if(ifon==2){  //散射
            numOfBullet=5;  //子弹数量
            int xPer=Math.abs(speedY)/numOfBullet;
            //散射子弹
            for(int i=0; i<numOfBullet; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                speedX=xPer*2*i+speedY;
                basicBullet = new HeroBullet(x + (i*2-5)*10, y, speedX, speedY, 30);
                res.add(basicBullet);
            }
        }else if(ifon==3){  //增加射击威力
            for(int i=0; i<1; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                basicBullet = new HeroBullet(x + (i*2)*10, y, speedX, speedY, 100);
                res.add(basicBullet);
            }
        }else{
            ((HeroAircraft) abstractAircraft).setIfon(0);
        }
        return res;
    }
}
