package World.Towers.Strategy;

import World.DrawableObject;
import World.HealthPoints;
import World.Time;
import World.Towers.Tower;

import java.util.ArrayList;

/**
 * Created by volverine on 5/18/16.
 */

public class Reincarnation implements Strategy {
    private long last_helath_revive;
    private long healh_rivive_speed = 1000;
    private HealthPoints healthPoints;
    private int heal_per_second = 1;
    private Time time;


    public Reincarnation(Tower tower, Time time) {
        this.time = time;
        this.healthPoints = tower.getHealthPoints();
        last_helath_revive = time.getCurrentGameTimeMillis();
    }

    public void Action(Tower tower, ArrayList<DrawableObject> mapObj) {
        if (time.timeGoneAfter(last_helath_revive, healh_rivive_speed)) {
            healthPoints.heal(heal_per_second);
            last_helath_revive = time.getCurrentGameTimeMillis();
        }
    }
}
