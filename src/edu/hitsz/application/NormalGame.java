package edu.hitsz.application;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.factory.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NormalGame extends Game{

    int addBlood=0; //用来给Boss机加点威力


    @Override
    public void action() {
        //完成英雄机的初始化
        HeroAircraft.getHeroAircraft().heroInit();
        score = 0;
        backGroundTop = 0;
        bgimage = ImageManager.BACKGROUND_IMAGE2;

        //是否加入背景音乐
        if (MusicChoose.getIfUseBgm()) {
            MusicRunnable.BGM_SOUND.setIfCycle(true);
            MusicRunnable.BGM_SOUND.setIfBreak(false);
            musicExecutorService.execute(MusicRunnable.BGM_SOUND);
        }

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            addBlood++; //Boss机血量增加

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 新敌机产生
                if (score > bEdge * bTimes && bossEnemySize(enemyAircrafts) < bossEnemyMaxNumber) {
                    //产生Boss机

                    //加入Boss音乐
                    MusicRunnable.BGM_BOSS_SOUND.setIfCycle(true);
                    MusicRunnable.BGM_BOSS_SOUND.setIfBreak(false);
                    musicExecutorService.execute(MusicRunnable.BGM_BOSS_SOUND);
                    new Thread(MusicRunnable.BGM_BOSS_SOUND).start();
                    enemyAircrafts.add(new BossEnemy(addBlood/2));
                    bTimes++;
                }
                if (score % 50 == 0 && score != 0 && eliteEnemySize(enemyAircrafts) < eliteEnemyMaxNumber+addBlood/400) {
                    enemyAircrafts.add((EliteEnemy) eliteEnemyFactory.produceFlyingObjectProduct());
                }
                if (enemyAircrafts.size() < enemyMaxNumber+addBlood/500) {
                    //根据分数产生精英机
                    enemyAircrafts.add((MobEnemy) mobEnemyFactory.produceFlyingObjectProduct());
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
}
