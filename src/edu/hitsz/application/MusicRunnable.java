package edu.hitsz.application;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author a
 */
public class MusicRunnable implements Runnable {

    //音频文件名
    private String filename;

    //音频文件格式
    private AudioFormat audioFormat;

    //存储数据
    private byte[] samples;

    //是否循环播放,默认为false，表示不循环播放，
    private boolean ifCycle=false;

    //用来进行退出播放,当它设置为true会中途退出播放
    private boolean ifBreak=false;


    public void setIfCycle(boolean ifCycle){
        this.ifCycle=ifCycle;
    }

    public void setIfBreak(boolean ifBreak){
        this.ifBreak=ifBreak;
    }


    //各种音频播放任务的定义定义，全是静态单例成员

    //总音频初始是循环播放的
    public static MusicRunnable BOMB_SOUND=new MusicRunnable("src/sounds/bomb_explosion.wav",true);
    public static MusicRunnable BGM_SOUND=new MusicRunnable("src/sounds/bgm.wav");
    public static MusicRunnable BGM_BOSS_SOUND=new MusicRunnable("src/sounds/bgm_boss.wav",true);
    public static MusicRunnable BULLET_SOUND=new MusicRunnable("src/sounds/bullet.wav");
    public static MusicRunnable BULLET_HIT_SOUND=new MusicRunnable("src/sounds/bullet_hit.wav");
    public static MusicRunnable GAME_OVER_SOUND=new MusicRunnable("src/sounds/game_over.wav");
    public static MusicRunnable GET_SUPPLY_SOUND=new MusicRunnable("src/sounds/get_supply.wav");


    private MusicRunnable(String filename) {
        //初始化filename
        this.filename = filename;
        reverseMusic();
    }

    private MusicRunnable (String filename,boolean ifCycle){
        //初始化filename
        this.filename = filename;
        reverseMusic();
        this.ifCycle=ifCycle;
    }

    public void reverseMusic() {
        try {
            //定义一个AudioInputStream用于接收输入的音频数据，使用AudioSystem来获取音频的音频输入流
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
            //用AudioFormat来获取AudioInputStream的格式
            audioFormat = stream.getFormat();
            samples = getSamples(stream);
        } catch (UnsupportedAudioFileException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] getSamples(AudioInputStream stream) {
        int size = (int) (stream.getFrameLength() * audioFormat.getFrameSize());
        byte[] samples = new byte[size];
        DataInputStream dataInputStream = new DataInputStream(stream);
        try {
            dataInputStream.readFully(samples);
        } catch (IOException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }
        return samples;
    }

    public void play(InputStream source) {
        int size = (int) (audioFormat.getFrameSize() * audioFormat.getSampleRate());
        byte[] buffer = new byte[size];
        //源数据行SourceDataLine是可以写入数据的数据行
        SourceDataLine dataLine = null;
        //获取受数据行支持的音频格式DataLine.info
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(audioFormat, size);
        } catch (LineUnavailableException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }
        dataLine.start();
        try {
            int numBytesRead = 0;
            while (numBytesRead != -1&&!ifBreak) {
				//从音频流读取指定的最大数量的数据字节，并将其放入缓冲区中
                numBytesRead =
                        source.read(buffer, 0, buffer.length);
				//通过此源数据行将数据写入混频器
                if (numBytesRead != -1) {
                    dataLine.write(buffer, 0, numBytesRead);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        dataLine.drain();
        dataLine.close();
    }

    @Override
    public void run() {

        do{
            InputStream stream = new ByteArrayInputStream(samples);
            play(stream);
        }while(ifCycle);
    }


    public static void main(String[] args) throws InterruptedException {
        MusicRunnable.BGM_SOUND.setIfCycle(true);
        MusicRunnable.BGM_SOUND.setIfBreak(false);
        Thread a=new Thread(MusicRunnable.BGM_BOSS_SOUND);
        a.setName("a线程");
        a.start();
        Thread.sleep(10000);
//            a.run();
        MusicRunnable.BGM_SOUND.setIfCycle(false);
        MusicRunnable.BGM_SOUND.setIfBreak(true);
//        Thread cur=Thread.currentThread();
        a.join();
//        System.out.println("结束");

    }


}


