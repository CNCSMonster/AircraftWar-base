
package edu.hitsz.prop;


import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;

import java.util.List;

public class PropBlood extends AbstractProp{



    public PropBlood(int x, int y) {
        super(x, y);
    }

    @Override
    public void propDo(HeroAircraft heroAircraft, List<AbstractAircraft> enemy) {
        //道具做事
        heroAircraft.decreaseHp(-100);
    }






    //重载父类的操作道具方法





}
