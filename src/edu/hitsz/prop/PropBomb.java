package edu.hitsz.prop;

public class PropBomb extends AbstractProp{

    public PropBomb(int x, int y) {
        super(x, y);
    }

    @Override
    public void propDo() {
        System.out.println("实现炸弹清屏");
    }
}
