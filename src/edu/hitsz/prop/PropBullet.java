package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.ScatterShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;

import java.util.List;

public class PropBullet extends AbstractProp{
    public PropBullet(int x, int y) {
        super(x, y);
    }

    //道具类作用对象是否是一样的


    @Override
    public void propDo(HeroAircraft heroAircraft, List<AbstractAircraft> enemy) {
        int a;
        a=(int)(Math.random()*3)+1;
        switch (a){
            case 1: //威力增加
                heroAircraft.setPower(100);
                break;
            case 2: //数量增加,并且直射
                heroAircraft.setNumOfBullet(5);
                heroAircraft.setShootStrategy(new StraightShootStrategy());
                break;
            case 3: //改变射击方式
                heroAircraft.setNumOfBullet(5);
                heroAircraft.setShootStrategy(new ScatterShootStrategy());
                break;
            default:
                break;
        }
        //开启一个线程计时结束英雄机子弹增加效果。
        Runnable runnable=()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            heroAircraft.setShootStrategy(new StraightShootStrategy());
            heroAircraft.setNumOfBullet(1);

        };
        new Thread(runnable).start();


    }


}
