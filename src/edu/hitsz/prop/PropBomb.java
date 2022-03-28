package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;

import java.util.List;

public class PropBomb extends AbstractProp{

    public PropBomb(int x, int y) {
        super(x, y);
    }

    @Override
    public void propDo(HeroAircraft heroAircraft, List<AbstractAircraft> enemy) {
        //清除所有飞机
        for(AbstractAircraft aircraft:enemy){
            if(!aircraft.notValid()){
                aircraft.vanish();
            }
        }
        System.out.println("BombSupply active!");

    }



}
