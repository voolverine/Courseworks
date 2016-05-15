package World.Towers;

import ApplicationGUI.ImageManager;
import World.DrawableObject;
import World.HealthPoints;
import World.Position;
import World.Time;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * Created by volverine on 5/12/16.
 */
public class MainTower extends Tower {
    public static Integer ImageID = new Integer(1000);
    private Bank bank;


    public MainTower(Position position, HealthPoints healthPoints, Time time) {
        super(position, healthPoints, time);
        bank = new Bank(1000);
    }


    public Bank getBank() {
        return bank;
    }

    public void Draw(GraphicsContext gc) {
        Image img = ImageManager.getInstance().getImage(ImageID);
        int image_x = position.getX() - (int)img.getWidth() / 2;
        int image_y = position.getY() - (int)img.getHeight() / 2;

        gc.drawImage(img, image_x, image_y);
    }


    public void Action(ArrayList<DrawableObject> mapObj) {}
}
