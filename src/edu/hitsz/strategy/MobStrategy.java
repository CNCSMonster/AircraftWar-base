package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BasicBullet;

import java.util.List;

public class MobStrategy implements Strategy{


    @Override
    public AbstractAircraft strategyGetAircraft() {
        return null;
    }

    @Override
    public List<BasicBullet> strategyShoot(AbstractAircraft abstractAircraft) {
        return null;
    }
}
