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

    //通知方法
    @Override
    public void propDo(){
        heroAircraft.getEffected(this);
    }




}
