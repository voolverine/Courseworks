package World.Towers;

import World.*;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * Created by volverine on 4/19/16.
 */
public class Tower extends DrawableObject implements IMovable, IHealthable {
    protected HealthPoints healthPoints;
    protected Time time;

    public HealthPoints getHealthPoints() {
        return healthPoints;
    }

    public Tower(Position position, HealthPoints healthPoints, Time time) {
        super(position);
        this.healthPoints = healthPoints;
        this.time = time;
    }

    public double getRadius() {
        return 0.0;
    }

    public int getSpeed() {
        return 0;
    }

    public int getBulletDamage() {
        return 0;
    }


    public void Draw(GraphicsContext gc) {}
    public void Action(ArrayList<DrawableObject> mapObj) {}
}
