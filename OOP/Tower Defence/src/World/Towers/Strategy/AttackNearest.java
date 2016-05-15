package World.Towers.Strategy;


import World.Bullets.Bullet;
import World.Bullets.LightUnitBullet;
import World.DrawableObject;
import World.Enemies.Enemy;
import World.Position;
import World.Time;
import World.Towers.LightUnitTower;
import World.Towers.Tower;
import javafx.geometry.Pos;

import java.sql.PseudoColumnUsage;
import java.util.ArrayList;

/**
 * Created by volverine on 5/12/16.
 */
public class AttackNearest implements Strategy {
    private Enemy target = null;
    private Time time;
    private long previous_second_of_shoot;


    public AttackNearest(Time time) {
        this.time = time;
        previous_second_of_shoot = time.getCurrentGameTimeSeconds();
    }


    private boolean isCloseEnough(Tower tower, Enemy enemy) {
        if (Position.dist(tower, enemy) < tower.getRadius()) {
            return true;
        } else {
            return false;
        }
    }


    private void choose_target(Tower tower, ArrayList<DrawableObject> mapObj) {
        Enemy best = null;
        double dist = 99999999;

        for (DrawableObject obj: mapObj) {
            if ((obj instanceof Enemy) && Position.dist(tower, obj) < dist) {
                dist = Position.dist(tower, obj);
                best = (Enemy)obj;
            }
        }

        if (best != null && isCloseEnough(tower, best)) {
            target = best;
        }
    }


    public void shoot(Tower tower, ArrayList<DrawableObject> mapObj) {
        LightUnitBullet bullet = new LightUnitBullet(new Position(tower.getPosition()),
                                                        target, tower.getBulletDamage());
        mapObj.add(bullet);
    }

    public void Action(Tower tower, ArrayList<DrawableObject> mapObj) {
        if (!time.timeGoneAfter(previous_second_of_shoot, tower.getSpeed())) {
            return;
        }


        if (target != null) {
            if (!isCloseEnough(tower, target) || target.getHealthPoints().isKilled()) {
                target = null;
            }
        }
        if (target == null) {
            choose_target(tower, mapObj);
        }

        if (target == null) {
            return;
        } else {
            shoot(tower, mapObj);
            previous_second_of_shoot = time.getCurrentGameTimeSeconds();
        }
    }
}
