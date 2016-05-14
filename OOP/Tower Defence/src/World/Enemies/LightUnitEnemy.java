package World.Enemies;

import ApplicationGUI.ImageManager;
import World.*;
import World.Towers.LightUnitTower;
import World.Towers.MainTower;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * Created by volverine on 5/11/16.
 */
public class LightUnitEnemy extends Enemy implements IHealthDrawable {
    public static Integer ImageID = new Integer(2000);
    private HealthProgress healthProgress;

    public LightUnitEnemy(Position position, HealthPoints healthPoints, MainTower mainTower) {
        super(position, healthPoints, mainTower);
        healthProgress = new HealthProgress(LightUnitTower.ImageID, healthPoints, position);
    }



    public void Draw(GraphicsContext gc) {
        Image img = ImageManager.getInstance().getImage(ImageID);
        int image_x = position.getX() - (int)img.getWidth() / 2;
        int image_y = position.getY() - (int)img.getHeight() / 2;

        gc.drawImage(img, image_x, image_y);
    }

    public void DrawHealth(GraphicsContext gc) {
        healthProgress.update(gc);
    }

    public void Action(ArrayList<DrawableObject> mapObj) {

    }
}
