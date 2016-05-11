package World.Enemies;

import World.DrawableObject;
import World.HealthPoints;
import World.Position;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by volverine on 5/11/16.
 */
public class Enemy extends DrawableObject {
    protected HealthPoints healthPoints;

    public HealthPoints getHealthPoints() {
        return healthPoints;
    }

    public Enemy(Position position, HealthPoints healthPoints) {
        super(position);
        this.healthPoints = healthPoints;
    }


    public void Draw(GraphicsContext gc) {}
    public void Action() {}
}
