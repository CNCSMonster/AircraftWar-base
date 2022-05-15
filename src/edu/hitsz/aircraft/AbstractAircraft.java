package edu.hitsz.aircraft;

import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.Strategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject{
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;

    //射击参数
    protected int power;
    protected int numOfBullet;

    public int getPower() {
        return power;
    }
    public void setPower(int power) {
        this.power = power;
    }
    public int getNumOfBullet() {
        return numOfBullet;
    }
    public void setNumOfBullet(int numOfBullet) {
        this.numOfBullet = numOfBullet;
    }

    //射击策略
    private Strategy shootStrategy;
    public void setShootStrategy(Strategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
    }

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp,Strategy strategy){
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        this.shootStrategy=strategy;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }else if(hp>maxHp){
            hp=maxHp;
        }
    }

    public int getHp() {
        return hp;
    }


    /**
     * 飞机射击方法，可射击对象必须实现
     * @return
     *  可射击对象需实现，返回子弹
     *  非可射击对象空实现，返回null
     */
    public List<BasicBullet> shoot(){
        return shootStrategy.strategyShoot(this);
    }

    // 重载父类的碰撞方法，使得专门针对子弹的撞击，
    public boolean crash(BasicBullet basicBullet) {   //重载了父类的方法
        // 缩放因子，用于控制 y轴方向区域范围
        int factor =2;
        int fFactor =1;

        int x = basicBullet.getLocationX();
        int y = basicBullet.getLocationY();
        int fWidth = basicBullet.getWidth();
        int fHeight = basicBullet.getHeight();
        if(     //如果发生了碰撞
                x + (fWidth+this.getWidth())/2 > locationX
                && x - (fWidth+this.getWidth())/2 < locationX
                && y + ( fHeight/fFactor+this.getHeight()/factor )/2 > locationY
                && y - ( fHeight/fFactor+this.getHeight()/factor )/2 < locationY
        ){
            return true;
        }else{
            return false;
        }
    }

    //使用观察者模式的观察方法,获得道具的影响
    public abstract void getEffected(AbstractProp abstractProp);
}


