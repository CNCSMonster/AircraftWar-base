package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BasicBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.dao.*;
import edu.hitsz.dao.Record;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.PropBlood;
import edu.hitsz.prop.PropBomb;
import edu.hitsz.prop.PropBullet;
import edu.hitsz.factory.*;
import edu.hitsz.strategy.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class Game extends JPanel {

    protected int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    protected ScheduledExecutorService executorService;

    ExecutorService musicExecutorService;




    //根据难度改变的地图
    BufferedImage bgimage;


    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected int timeInterval = 40;


    protected HeroAircraft heroAircraft;
    protected List<AbstractAircraft> enemyAircrafts;
    protected List<BasicBullet> heroBullets;
    protected List<BasicBullet> enemyBullets;
    protected List<AbstractProp> abstractProps;
    protected AbstractAircraftFactory mobEnemyFactory;
    protected AbstractAircraftFactory eliteEnemyFactory;
    protected AbstractAircraftFactory bossEnemyFactory;
    protected AbstractPropFactory propBloodFactory;
    protected AbstractPropFactory propBulletFactory;
    protected AbstractPropFactory propBombFactory;




    protected int enemyMaxNumber = 5;
    protected int eliteEnemyMaxNumber=1; //限制精英敌机的数量
    protected int bossEnemyMaxNumber=1;   //限制boss机的数量
    protected int bTimes=0;  //记录boss机出现的次数
    protected int bEdge=200;   //记录boss机出现的分数阈值

//    private boolean gameOverFlag = false;
//    private int score = 0;
//    private int time = 0;

    boolean gameOverFlag = false;
    int score = 0;
    int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    protected int cycleDuration = 600;
    protected int cycleTime = 0;


    public Game() {
        heroAircraft =HeroAircraft.getHeroAircraft();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        abstractProps = new LinkedList<>();



        //初始化观察者模式需要的值
        AbstractProp.setHeroAircraft(heroAircraft);
        AbstractProp.setHeroBullets(heroBullets);
        AbstractProp.setEnemyAircrafts(enemyAircrafts);
        AbstractProp.setEnemyBullets(enemyBullets);

      //策略模式使用抽象飞机作为上下文


        //对工厂进行初始化
        bossEnemyFactory =new BossEnemyFactory();
        mobEnemyFactory = new MobEnemyFactory();
        eliteEnemyFactory =new EliteEnemyFactory();
        propBloodFactory =new PropBloodFactory();
        propBulletFactory =new PropBulletFactory();
        propBombFactory =new PropBombFactory();

        //Scheduled 线程池，用于定时任务
        executorService = new ScheduledThreadPoolExecutor(1);

        //假如不用线程池播放音乐
        //线程池，用于音乐
        musicExecutorService=new ThreadPoolExecutor(15,20,60L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);
    }






    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public abstract void action();

    //游戏中途结束
    public void endGame(){
        //结束两个线程
        //如果两个线程没有结束的话
        if(!executorService.isTerminated()){
            executorService.shutdown();
        }
        if(!musicExecutorService.isTerminated()){
            musicExecutorService.shutdown();
        }
        //关闭可能的Boss音乐和背景音乐
        MusicRunnable.BGM_BOSS_SOUND.setIfBreak(true);
        MusicRunnable.BGM_BOSS_SOUND.setIfCycle(false);
        MusicRunnable.BGM_SOUND.setIfCycle(false);
        MusicRunnable.BGM_SOUND.setIfBreak(true);

    }



    protected void propMoveAction() {
        for(AbstractProp abstractProp:abstractProps){
            abstractProp.forward();
        }
    }

    //***********************
    //      Action 各部分
    //***********************

    protected boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    // 判断各种类型的敌机的数量
    protected int eliteEnemySize(List<AbstractAircraft> abstractAircrafts){
        assert abstractAircrafts!=null;
        int out=0;
        for(AbstractAircraft aircraft:abstractAircrafts){
            if(aircraft.notValid()){
                continue;
            }
            if(aircraft instanceof EliteEnemy){
                out+=1;
            }
        }
        return out;
    }
    protected int bossEnemySize(List<AbstractAircraft> abstractAircrafts){
        assert abstractAircrafts!=null;
        int out=0;
        for(AbstractAircraft aircraft:abstractAircrafts){
            if(aircraft.notValid()){
                continue;
            }
            if(aircraft instanceof BossEnemy){
                out+=1;
            }
        }
        return out;
    }




    protected void shootAction() {
        // 敌机射击
        for(AbstractAircraft enemy:enemyAircrafts){
            enemyBullets.addAll(enemy.shoot());
        }
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    protected void bulletsMoveAction() {
        for (BasicBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BasicBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    protected void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    protected void crashCheckAction() {
        // 敌机子弹攻击英雄
        for (BasicBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            // 判断敌人子弹是否射击到英雄
            if(heroAircraft.crash(bullet)){
                bullet.vanish();
                heroAircraft.decreaseHp(bullet.getPower());
            }
        }

        // 英雄子弹攻击敌机和敌人撞击英雄
        for (BasicBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    new Thread(MusicRunnable.BULLET_HIT_SOUND).start();
//                    executorService.execute(MusicRunnable.BULLET_HIT_SOUND);
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    if(enemyAircraft instanceof BossEnemy&&enemyAircraft.notValid()){
                        //如果有Boss机被击毁
                        MusicRunnable.BGM_BOSS_SOUND.setIfBreak(true);
                        MusicRunnable.BGM_BOSS_SOUND.setIfCycle(false);
                        //并且增加分数
                        //分数与这时Boss机的血量成正比
                        score+=enemyAircraft.getHp();
                    }
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // 获得分数，产生道具补给
                        // 根据敌机的不同，产生不同的分数
                        if(enemyAircraft instanceof EliteEnemy){
                            score+=50;
                            //获得道具
                            getProps();
                        }else{
                            score+=10;
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        //  我方获得道具，道具生效
        for(AbstractProp abstractProp:abstractProps){
            if(heroAircraft.crash(abstractProp)&&abstractProp.crash(heroAircraft)){
                //如果发生了撞击
                abstractProp.propDo();
                new Thread(MusicRunnable.GET_SUPPLY_SOUND).start();
//                musicExecutorService.execute(MusicRunnable.GET_SUPPLY_SOUND);
            }
        }
    }

    //获得道具补给
    protected void getProps(){
        int rand=(int)(Math.random()*14);
        if(rand<3){
            abstractProps.add((PropBlood)propBloodFactory.produceFlyingObjectProduct());
        }else if(rand<4){
            abstractProps.add((PropBomb)propBombFactory.produceFlyingObjectProduct());
        }else if(rand<13){
            abstractProps.add((PropBullet)propBulletFactory.produceFlyingObjectProduct());
        }
    }


    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    protected void postProcessAction() {

        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        abstractProps.removeIf(AbstractFlyingObject::notValid);





    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(bgimage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(bgimage, 0, this.backGroundTop, null);


        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，再绘制道具，后绘制飞机
        // 绘制道具
        paintImageWithPositionRevised(g,abstractProps);

        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, enemyAircrafts);

        //画图里面传入的参数是从左上角起点开始画的。
        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
//        int x=0;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}
