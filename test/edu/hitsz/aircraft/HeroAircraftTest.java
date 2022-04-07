package edu.hitsz.aircraft;

import edu.hitsz.bullet.BasicBullet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {

    @Test
    void getHeroAircraft() {
        HeroAircraft a=HeroAircraft.getHeroAircraft();
        HeroAircraft b=HeroAircraft.getHeroAircraft();
        System.out.println(a.getHp());
        a.decreaseHp(1);
        System.out.println(a.getHp());
        System.out.println(b.getHp());
    }

    @Test
    void shoot() {
        HeroAircraft h=HeroAircraft.getHeroAircraft();
        List<BasicBullet> a=h.shoot();
        System.out.println("x:"+h.getLocationX()+",y:"+h.getLocationY());
        for(BasicBullet basicBullet:a){
            System.out.println("x:"+basicBullet.getLocationX()+",y:"+basicBullet.getLocationY());
        }
     }
}