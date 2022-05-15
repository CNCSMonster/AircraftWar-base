
package edu.hitsz.prop;


import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;

import java.util.List;


/**
 * @author
 */
public class PropBlood extends AbstractProp{



    public PropBlood(int x, int y) {
        super(x, y);
    }



    //通知方法
    @Override
    public void propDo(){
        heroAircraft.getEffected(this);
    }








}
