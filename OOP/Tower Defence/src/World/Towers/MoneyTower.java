package World.Towers;

import ApplicationGUI.ImageManager;
import World.HealthPoints;
import World.Position;
import javafx.animation.Interpolatable;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Created by volverine on 5/11/16.
 */
public class MoneyTower extends Tower {
    public static Integer ImageID = new Integer(1001);

    public MoneyTower(Position position, HealthPoints healthPoints) {
        super(position, healthPoints);
    }


    public void Draw(GraphicsContext gc) {
        Image img = ImageManager.getInstance().getImage(ImageID);
        int image_x = position.getX() - (int)img.getWidth() / 2;
        int image_y = position.getY() - (int)img.getHeight() / 2;

        gc.drawImage(img, image_x, image_y);
    }

    public void Action() {}
}
