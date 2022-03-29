package edu.hitsz.factory;

import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public interface AbstractAircraftFactory{
    public abstract FlyingObjectProduct produceFlyingObjectProduct();

}
