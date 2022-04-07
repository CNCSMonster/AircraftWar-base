package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.EnemyBullet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropBloodTest {

    @Test
    void crash() {
        HeroAircraft heroAircraft=HeroAircraft.getHeroAircraft();
        PropBlood propBlood=new PropBlood(heroAircraft.getLocationX(),heroAircraft.getLocationY());
        System.out.println(propBlood.notValid());
        propBlood.crash(heroAircraft);
        System.out.println(propBlood.notValid());
    }

    @Test
    void propDo() {
        HeroAircraft heroAircraft=HeroAircraft.getHeroAircraft();
        PropBlood propBlood=new PropBlood(heroAircraft.getLocationX(),heroAircraft.getLocationY());
        System.out.println(heroAircraft.getHp());
        propBlood.propDo(heroAircraft,null);
        System.out.println(heroAircraft.getHp());
    }
}