package World.Towers;

import World.DrawableObject;
import World.HealthPoints;
import World.IMovable;
import World.Position;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * Created by volverine on 4/19/16.
 */
public class Tower extends DrawableObject implements IMovable {
    protected HealthPoints healthPoints;

    public HealthPoints getHealthPoints() {
        return healthPoints;
    }

    public Tower(Position position, HealthPoints healthPoints) {
        super(position);
        this.healthPoints = healthPoints;
    }


    public void Draw(GraphicsContext gc) {}
    public void Action(ArrayList<DrawableObject> mapObj) {}
}
