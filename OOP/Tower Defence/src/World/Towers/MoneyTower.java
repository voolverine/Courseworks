package World.Towers;

import ApplicationGUI.ImageManager;
import World.DrawableObject;
import World.HealthPoints;
import World.Position;
import javafx.animation.Interpolatable;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * Created by volverine on 5/11/16.
 */
public class MoneyTower extends Tower {
    public static Integer ImageID = new Integer(1001);
    private int before_money = 3600;
    private MainTower mainTower;
    private int salary = 25;

    public MoneyTower(Position position, HealthPoints healthPoints, MainTower mainTower) {
        super(position, healthPoints);
        this.mainTower = mainTower;
    }


    public void Draw(GraphicsContext gc) {
        Image img = ImageManager.getInstance().getImage(ImageID);
        int image_x = position.getX() - (int)img.getWidth() / 2;
        int image_y = position.getY() - (int)img.getHeight() / 2;

        gc.drawImage(img, image_x, image_y);
    }


    public void Action(ArrayList<DrawableObject> mapObj) {
        before_money--;
        if (before_money <= 0) {
            before_money = 3600;
            mainTower.getBank().invest(salary);
        }
    }
}
