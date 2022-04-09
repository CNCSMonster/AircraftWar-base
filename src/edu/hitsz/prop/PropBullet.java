package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;

import java.util.List;

public class PropBullet extends AbstractProp{
    public PropBullet(int x, int y) {
        super(x, y);
    }

    @Override
    public void propDo(HeroAircraft heroAircraft, List<AbstractAircraft> enemy) {
        int a;
        a=(int)(Math.random()*3)+1;
        heroAircraft.setIfon(a);
    }


}
