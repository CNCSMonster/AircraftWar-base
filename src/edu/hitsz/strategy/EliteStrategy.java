package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class EliteStrategy implements Strategy{
    @Override
    public AbstractAircraft strategyGetAircraft() {
        return new EliteEnemy(
                (int) ( Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth()))*1,
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1
        );
    }

    @Override
    public List<BasicBullet> strategyShoot(AbstractAircraft abstractAircraft) {
        var ifon=((HeroAircraft)abstractAircraft).getIfon();
        List<BasicBullet> res = new LinkedList<>();
        int x = abstractAircraft.getLocationX();
        int y = abstractAircraft.getLocationY() -2;
        int speedX = 0;
        int speedY = -5;
        BasicBullet basicBullet;
        if(ifon==0){
            for(int i=0; i<1; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                basicBullet = new HeroBullet(x + (i*2)*10, y, speedX, speedY, 10);
                res.add(basicBullet);
            }
        }else{
            for(int i=0; i<5; i++){
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                basicBullet = new HeroBullet(x + (i*2-5)*10, y, speedX, speedY, 30);
                res.add(basicBullet);
            }
        }
        return res;
    }
}
