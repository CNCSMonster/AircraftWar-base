package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BasicBullet;

import java.util.List;

public class PropBomb extends AbstractProp{

    public PropBomb(int x, int y) {
        super(x, y);
    }




    @Override
    public void propDo(){
        for(AbstractAircraft abstractAircraft:enemyAircrafts){
            abstractAircraft.getEffected(this);
        }
        for(BasicBullet basicBullet:enemyBullets){
            basicBullet.getEffected(this);
        }
        for(BasicBullet basicBullet:heroBullets){
            basicBullet.getEffected(this);
        }

    }
}
