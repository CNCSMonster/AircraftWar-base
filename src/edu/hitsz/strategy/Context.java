package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.strategy.Strategy;

import java.util.List;

/**
 * @author a
 */
public class Context {

    private Strategy strategy;

    public Context(Strategy strategy){
        this.strategy=strategy;
    }

    public void setStrategy(Strategy strategy){
        this.strategy=strategy;
    }

    public List<BasicBullet> useShootStrategy(AbstractAircraft abstractAircraft){
        return this.strategy.strategyShoot(abstractAircraft);
    }

}