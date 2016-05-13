package World.Towers;

import ApplicationGUI.ImageManager;
import World.DrawableObject;
import World.HealthPoints;
import World.Position;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.omg.PortableServer.POA;

import java.util.ArrayList;

/**
 * Created by volverine on 5/11/16.
 */
public class LightUnitTower extends Tower {
    public static Integer ImageID = new Integer(1002);
    private MainTower mainTower;

    public LightUnitTower(Position position, HealthPoints healthPoints, MainTower mainTower) {
        super(position, healthPoints);
        this.mainTower = mainTower;
    }


    public void Draw(GraphicsContext gc) {
        Image img = ImageManager.getInstance().getImage(ImageID);
        int image_x = position.getX() - (int)img.getWidth() / 2;
        int image_y = position.getY() - (int)img.getHeight() / 2;

        gc.drawImage(img, image_x, image_y);
    }

    public void Action(ArrayList<DrawableObject> mapObj) {}
}
