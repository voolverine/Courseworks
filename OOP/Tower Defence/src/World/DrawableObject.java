package World;


import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by volverine on 4/19/16.
 */
public class DrawableObject {
    protected Position position;

    public Position getPosition() {
        return position;
    }

    public DrawableObject(Position position) {
        this.position = position;
    }


    public void Draw(GraphicsContext gc) {}
    public void Action() {}
}
