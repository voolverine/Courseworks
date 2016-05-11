package World.Towers;

import World.DrawableObject;
import World.HealthPoints;
import World.Position;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by volverine on 4/19/16.
 */
public class Tower extends DrawableObject {
    protected HealthPoints healthPoints;

    public HealthPoints getHealthPoints() {
        return healthPoints;
    }

    public Tower(Position position, HealthPoints healthPoints) {
        super(position);
        this.healthPoints = healthPoints;
    }


    public void Draw(GraphicsContext gc) {}
    public void Action() {}
}
