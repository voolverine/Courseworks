package World.Enemies;

import ApplicationGUI.ImageManager;
import World.*;
import World.Enemies.Strategy.AttackNearest;
import World.Enemies.Strategy.ForwardMainTower;
import World.Towers.LightUnitTower;
import World.Towers.MainTower;
import World.Enemies.Strategy.Strategy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * Created by volverine on 5/11/16.
 */
public class LightUnitEnemy extends Enemy implements IHealthDrawable {
    public static Integer ImageID = new Integer(2000);
    private HealthProgress healthProgress;
    Strategy forwardStrategy;
    Strategy attackStrategy;
    private int damage = 4;
    private int Radius = 100;
    private int atackSpeed = 4;
    private int movingSpeed = 3;

    public int getBulletDamage() {
        return damage;
    }

    public double getRadius() {
        return Radius;
    }

    public int getAtackSpeed() {
        return atackSpeed;
    }


    public void setPosition(int new_x, int new_y) {
        position.setX(new_x);
        position.setY(new_y);
    }


    public LightUnitEnemy(Position position, HealthPoints healthPoints, MainTower mainTower, Time time) {
        super(position, healthPoints, mainTower, time);
        healthProgress = new HealthProgress(LightUnitTower.ImageID, healthPoints, position);
        forwardStrategy = new ForwardMainTower(this);
        attackStrategy = new AttackNearest(this, time);
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
        forwardStrategy.Action(mapObj);
        attackStrategy.Action(mapObj);
    }
}
