package edu.hitsz.aircraft;

import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.strategy.Strategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;

    //引入具体策略
    private Strategy shootStrategy;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
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
    public abstract List<BasicBullet> shoot();

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

}


