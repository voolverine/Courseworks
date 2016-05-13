package World.Bullets;

import World.*;
import World.Enemies.Enemy;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * Created by volverine on 5/13/16.
 */
public class Bullet extends DrawableObject implements IMovable, IHealthable {
    DrawableObject target;

    protected int damage;
    protected HealthPoints healthPoints;

    public HealthPoints getHealthPoints() {
        return healthPoints;
    }

    public Bullet(Position position, DrawableObject target, int damage) {
        super(position);
        this.target = target;
        this.damage = damage;
    }

    public void Draw(GraphicsContext gc) {}
    public void Action(ArrayList<DrawableObject> mapObj) {}
}
