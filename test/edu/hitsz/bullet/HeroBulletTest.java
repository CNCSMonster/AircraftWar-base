package edu.hitsz.bullet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeroBulletTest {

    @Test
    void forward() {
        HeroBullet a=new HeroBullet(2,2,0,1,3);
        System.out.println("x:"+a.getLocationX()+",y:"+a.getLocationY());
        a.forward();
        System.out.println("x:"+a.getLocationX()+",y:"+a.getLocationY());

    }

    @Test
    void getPower() {
        HeroBullet a=new HeroBullet(2,2,0,1,3);
        System.out.println(a.getPower());
    }
}