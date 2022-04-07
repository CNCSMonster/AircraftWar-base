package edu.hitsz.prop;


import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;

import java.util.List;

public abstract class AbstractProp extends AbstractFlyingObject {


    // 重载生成道具类型的方法
    public AbstractProp(int x,int y){
        super(x,y,0,0);
    }



    // 重载crash方法，专门用来实现道具与英雄机的碰撞
    public boolean crash(HeroAircraft heroAircraft){
        int x=heroAircraft.getLocationX();
        int y=heroAircraft.getLocationY();
        int width=heroAircraft.getWidth();
        int height=heroAircraft.getHeight();
        if(
                this.locationX+(this.width+width)/2<x
                ||this.locationX-(this.width+width)/2>x
                ||this.locationY+(this.height+height)/2<y
                ||this.locationY-(this.height+height)/2>y
        ){  //如果没有发生碰撞
            return false;
        }else{
            //如果发生碰撞了，实现该类型的无效化
            this.vanish();
            return true;
        }
    }

    public abstract void propDo(HeroAircraft heroAircraft, List<AbstractAircraft> enemy);//定义一个抽象方法实现道具的效果

    // 实现抽象父类中的移动方法
    @Override
    public void forward(){
        //作为道具，不需要移动
    }

}
