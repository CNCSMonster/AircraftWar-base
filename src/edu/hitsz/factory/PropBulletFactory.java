package edu.hitsz.factory;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.prop.PropBullet;

public class PropBulletFactory implements AbstractPropFactory{

    @Override
    public FlyingObjectProduct produceFlyingObjectProduct() {
        return new PropBullet(
                (int) ( Math.random() * (Main.WINDOW_WIDTH - ImageManager.Prop_Blood_IMAGE.getWidth()))*1,
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2+Main.WINDOW_HEIGHT*0.6)*1
        );
    }
}
