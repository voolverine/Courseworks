package World.Enemies.Strategy;


import World.DrawableObject;
import World.Enemies.Enemy;
import World.Position;
import World.Towers.Tower;
import javafx.geometry.Pos;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by volverine on 5/12/16.
 */
public class AttackNearest implements Strategy {
   Enemy enemy;

   public AttackNearest(Enemy enemy) {
      this.enemy = enemy;
   }

   public void Action(ArrayList<DrawableObject> mapObj) {

   }
}
