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
public class Game extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    private final ExecutorService musicExecutorService;


    //难度设置,不同难度对应不同背景//初始为0难度，共分5种难度
    private static int levelOfGame=0;

    //根据难度改变的地图
    private static BufferedImage bgimage;

    public static void setLevel(int levelOfGame){
        Game.levelOfGame=levelOfGame;
    }
    //获得当前难度
    public static String getLevel(){
        switch(Game.levelOfGame){
            case 0:
                return "简单";
            case 1:
                return "中等";
            case 2:
                return "困难";
            default:
                return "其他";
        }
    }

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;


    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BasicBullet> heroBullets;
    private final List<BasicBullet> enemyBullets;
    private final List<AbstractProp> abstractProps;
    private final AbstractAircraftFactory mobEnemyFactory;
    private final AbstractAircraftFactory eliteEnemyFactory;
    private final AbstractAircraftFactory bossEnemyFactory;
    private final AbstractPropFactory propBloodFactory;
    private final AbstractPropFactory propBulletFactory;
    private final AbstractPropFactory propBombFactory;




    private int enemyMaxNumber = 5;
    private int eliteEnemyMaxNumber=1; //限制精英敌机的数量
    private int bossEnemyMaxNumber=1;   //限制boss机的数量
    private int bTimes=0;  //记录boss机出现的次数
    private int bEdge=200;   //记录boss机出现的分数阈值

    private boolean gameOverFlag = false;
    private int score = 0;
    private int time = 0;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;


    public Game() {
        heroAircraft =HeroAircraft.getHeroAircraft();

        enemyAircrafts = new LinkedList<>();

        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        abstractProps = new LinkedList<>();





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

        //线程池，用于音乐
        musicExecutorService=new ThreadPoolExecutor(5,10,60L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);
    }






    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        //完成英雄机的初始化
        HeroAircraft.getHeroAircraft().heroInit();
        score=0;
        backGroundTop=0;
        switch (levelOfGame){
            case 0:
                bgimage=ImageManager.BACKGROUND_IMAGE;
                break;
            case 1:
                bgimage=ImageManager.BACKGROUND_IMAGE2;
                break;
            case 2:
                bgimage=ImageManager.BACKGROUND_IMAGE3;
                break;
            default:
                bgimage=ImageManager.BACKGROUND_IMAGE4;
        }
        //是否加入背景音乐
        if(MusicChoose.getIfUseBgm()){
            MusicRunnable.BGM_SOUND.setIfCycle(true);
            MusicRunnable.BGM_SOUND.setIfBreak(false);
            musicExecutorService.execute(MusicRunnable.BGM_SOUND);
        }

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 新敌机产生
                if(score>bEdge* bTimes &&bossEnemySize(enemyAircrafts)<bossEnemyMaxNumber){
                    //产生Boss机

                    //加入Boss音乐
                    MusicRunnable.BGM_BOSS_SOUND.setIfCycle(true);
                    MusicRunnable.BGM_BOSS_SOUND.setIfBreak(false);
                    musicExecutorService.execute(MusicRunnable.BGM_BOSS_SOUND);

                    enemyAircrafts.add((BossEnemy)bossEnemyFactory.produceFlyingObjectProduct());
                    bTimes++;
                }
                if(score%50==0&&score!=0&&eliteEnemySize(enemyAircrafts)<eliteEnemyMaxNumber){
                    enemyAircrafts.add((EliteEnemy)eliteEnemyFactory.produceFlyingObjectProduct());
                }
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    //根据分数产生精英机
                        enemyAircrafts.add((MobEnemy)mobEnemyFactory.produceFlyingObjectProduct());
                }
                // 飞机射出子弹，包括英雄机射出子弹和敌机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            //道具移动
            propMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                //结束背景音乐的播放
                MusicRunnable.BGM_SOUND.setIfCycle(false);
                MusicRunnable.BGM_SOUND.setIfBreak(true);

                //关闭可能在循环播放的Boss背景音乐
                MusicRunnable.BGM_BOSS_SOUND.setIfCycle(false);
                MusicRunnable.BGM_BOSS_SOUND.setIfBreak(true);

                //新开启一个线程播放结束音乐
                musicExecutorService.execute(MusicRunnable.GAME_OVER_SOUND);
                executorService.shutdown();

                gameOverFlag = true;

                try {
                    new RecordDialog(score);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
    }

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



    private void propMoveAction() {
        for(AbstractProp abstractProp:abstractProps){
            abstractProp.forward();
        }
    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
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
    private int eliteEnemySize(List<AbstractAircraft> abstractAircrafts){
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
    private int bossEnemySize(List<AbstractAircraft> abstractAircrafts){
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




    private void shootAction() {
        // 敌机射击
        for(AbstractAircraft enemy:enemyAircrafts){
            enemyBullets.addAll(enemy.shoot());
        }
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void bulletsMoveAction() {
        for (BasicBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BasicBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
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
    private void crashCheckAction() {
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
                    executorService.execute(MusicRunnable.BULLET_HIT_SOUND);
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    if(enemyAircraft instanceof BossEnemy&&enemyAircraft.notValid()){
                        //如果有Boss机被击毁
                        MusicRunnable.BGM_BOSS_SOUND.setIfBreak(true);
                        MusicRunnable.BGM_BOSS_SOUND.setIfCycle(false);
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
                abstractProp.propDo(heroAircraft,enemyAircrafts);
                musicExecutorService.execute(MusicRunnable.GET_SUPPLY_SOUND);
            }
        }
    }

    //获得道具补给
    private void getProps(){
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
    private void postProcessAction() {

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
