package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BasicBullet;

import java.util.List;

public interface Strategy {


    //根据策略生成不同的设计效果
    public abstract List<BasicBullet> strategyShoot(AbstractAircraft abstractAircraft);

    

}
