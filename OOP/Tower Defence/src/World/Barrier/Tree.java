package World.Barrier;

import World.DrawableObject;
import World.HealthPoints;
import World.IHealthable;
import World.Position;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by volverine on 5/18/16.
 */
public class Tree extends DrawableObject implements IHealthable {
    HealthPoints healthPoints;

    public HealthPoints getHealthPoints() {
        return healthPoints;
    }

    public Tree(Position position, HealthPoints healthPoints) {
        super(position);
        this.healthPoints = healthPoints;
    }

    public void Draw(GraphicsContext gc) {}
}
