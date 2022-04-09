package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.ScatterShootStrategy;

import java.util.List;

public class PropBullet extends AbstractProp{
    public PropBullet(int x, int y) {
        super(x, y);
    }

    @Override
    public void propDo(HeroAircraft heroAircraft, List<AbstractAircraft> enemy) {
        int a;
        a=(int)(Math.random()*3)+1;
        a=1;
        switch (a){
            case 1: //威力增加
                heroAircraft.setPower(100);
                break;
            case 2: //数量增加
                heroAircraft.setNumOfBullet(5);
                break;
            case 3: //改变射击方式
                heroAircraft.setShootStrategy(new ScatterShootStrategy());
                break;
            default:
                break;
        }
    }


}
