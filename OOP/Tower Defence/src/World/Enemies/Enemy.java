package World.Enemies;

import World.*;
import World.Towers.MainTower;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Pair;
import sun.awt.image.ImageWatched;
import sun.awt.image.IntegerComponentRaster;

import javax.crypto.spec.PSource;
import java.util.*;

/**
 * Created by volverine on 5/11/16.
 */
public class Enemy extends DrawableObject implements IMovable, IHealthable {

    protected HealthPoints healthPoints;
    protected MainTower mainTower;
    protected Time time;

    public HealthPoints getHealthPoints() {
        return healthPoints;
    }

    public MainTower getMainTower() {
        return mainTower;
    }

    public int getBulletDamage() {
        return 0;
    }

    public double getRadius() {
        return 0;
    }

    public int getAtackSpeed() {
        return 0;
    }

    public int getMovingSpeed() {
        return 0;
    }

    public void setPosition(int new_x, int new_y) {
        position.setX(new_x);
        position.setY(new_y);
    }

    public Enemy(Position position, HealthPoints healthPoints, MainTower mainTower, Time time) {
        super(position);
        this.healthPoints = healthPoints;
        this.mainTower = mainTower;
        this.time = time;
    }


    public void Draw(GraphicsContext gc) {}
    public void Action(ArrayList<DrawableObject> mapObj) {}
}
