package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.PropBlood;
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


    //使用观察者模式的观察方法,获得道具的影响
    @Override
    public void getEffected(AbstractProp abstractProp){
        //射击策略或者子弹数量改变
        if(abstractProp instanceof PropBlood){
            decreaseHp(-5000);
        }else{
            int a;
            a=(int)(Math.random()*3)+1;
            switch (a){
                case 1: //威力增加
                    setPower(100);
                    break;
                case 2: //数量增加,并且直射
                    setNumOfBullet(5);
                    setShootStrategy(new StraightShootStrategy());
                    break;
                case 3: //改变射击方式
                    setNumOfBullet(5);
                    setShootStrategy(new ScatterShootStrategy());
                    break;
                default:
                    break;
            }
            //开启一个线程计时结束英雄机子弹增加效果。
            Runnable runnable=()->{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setShootStrategy(new StraightShootStrategy());
                setNumOfBullet(1);

            };
            new Thread(runnable).start();
        }
    }


}
