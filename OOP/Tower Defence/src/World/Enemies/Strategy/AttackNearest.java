package World.Enemies.Strategy;


import World.Bullets.LightUnitBullet;
import World.DrawableObject;
import World.Enemies.Enemy;
import World.Position;
import World.Time;
import World.Towers.MainTower;
import World.Towers.Tower;
import javafx.geometry.Pos;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by volverine on 5/12/16.
 */
public class AttackNearest implements Strategy {
   private Enemy enemy;
   private Time time;
   private long previous_atack_time;
   private Tower target;

   public AttackNearest(Enemy enemy, Time time) {
      this.enemy = enemy;
      this.time = time;
      previous_atack_time = time.getCurrentGameTimeSeconds();
   }

   private void shoot(ArrayList<DrawableObject> mapObj) {
        LightUnitBullet bullet = new LightUnitBullet(new Position(enemy.getPosition()),
                                                        target, enemy.getBulletDamage());
        mapObj.add(bullet);
   }


   private boolean isCloseEnough(Tower tower) {
       if (Position.dist(tower, enemy) < enemy.getRadius()) {
           return true;
       } else {
           return false;
       }
   }


    void find_target(ArrayList<DrawableObject> mapObj) {

        Tower best = null;
        double dist = 99999999;

        for (DrawableObject obj: mapObj) {
            if ((obj instanceof Tower) && Position.dist(enemy, obj) < dist) {
                dist = Position.dist(enemy, obj);
                best = (Tower)obj;
            }
        }

        if (best != null && isCloseEnough(best)) {
            target = best;
        }

    }

    public void Action(ArrayList<DrawableObject> mapObj) {
        if (!time.timeGoneAfter(previous_atack_time, enemy.getAtackSpeed())) {
            return;
        }


        if (isCloseEnough(enemy.getMainTower())) {
            target = enemy.getMainTower();
        }

        if (target == null || !isCloseEnough(target) ||  target.getHealthPoints().isKilled()) {
            target = null;
            find_target(mapObj);
        }

        if (target != null && isCloseEnough(target)) {
            shoot(mapObj);
            previous_atack_time = time.getCurrentGameTimeSeconds();
        }
    }
}
