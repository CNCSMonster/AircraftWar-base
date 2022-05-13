package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.strategy.ScatterShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /** 攻击方式 */

    private static HeroAircraft heroAircraft =new HeroAircraft(
            Main.WINDOW_WIDTH / 2,
            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
            0, 0, 10000);
    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.power=30;
        this.numOfBullet=1;
//        this.setShootStrategy(new ScatterShootStrategy());
        this.setShootStrategy(new StraightShootStrategy());
    }

    public void heroInit(){
        this.locationX=Main.WINDOW_WIDTH / 2;
        this.locationY=Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight();
        this.hp=10000;
    }


    public static HeroAircraft getHeroAircraft(){
        return heroAircraft;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }




}
