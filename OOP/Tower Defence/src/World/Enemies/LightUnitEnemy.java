package World.Enemies;

import ApplicationGUI.ImageManager;
import World.HealthPoints;
import World.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Created by volverine on 5/11/16.
 */
public class LightUnitEnemy extends Enemy {
    private Integer ImageID = new Integer(2000);

    public LightUnitEnemy(Position position, HealthPoints healthPoints) {
        super(position, healthPoints);
    }


    public void Draw(GraphicsContext gc) {
        Image img = ImageManager.getInstance().getImage(ImageID);
        int delta_width = (int)img.getWidth() / 2;
        int delta_height = (int)img.getHeight() / 2;

        gc.drawImage(img, this.getPosition().getX(), this.getPosition().getY());
    }
    public void Action() {}
}
