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
        System.out.println("FireSupply active!");
    }


}
