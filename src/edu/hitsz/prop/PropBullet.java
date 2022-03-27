package edu.hitsz.prop;

public class PropBullet extends AbstractProp{
    public PropBullet(int x, int y) {
        super(x, y);
    }

    @Override
    public void propDo() {
        System.out.println("获得特殊子弹");
    }
}
