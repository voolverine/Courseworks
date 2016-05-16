package World.Towers;

import ApplicationGUI.ImageManager;
import World.*;
import World.Towers.Strategy.AttackNearest;
import World.Towers.Strategy.Strategy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * Created by volverine on 5/15/16.
 */
public class HeavyUnitTower extends Tower implements IHealthDrawable {
    public static Integer ImageID = new Integer(1003);
    private MainTower mainTower;
    private double Radius = 100;
    private int damage = 10;
    private int speed = 3000;
    private Strategy strategy;
    private HealthProgress healthProgress;

    public double getRadius() {
        return Radius;
    }

    public int getSpeed() {
        return speed;
    }

    public int getBulletDamage() {
        return damage;
    }

    public HeavyUnitTower(Position position, HealthPoints healthPoints, MainTower mainTower, Time time) {
        super(position, healthPoints, time);
        this.mainTower = mainTower;
        healthProgress = new HealthProgress(LightUnitTower.ImageID, healthPoints, position);
        strategy = new AttackNearest(time);
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
        strategy.Action(this, mapObj);
    }
}
