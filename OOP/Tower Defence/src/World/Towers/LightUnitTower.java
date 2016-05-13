package World.Towers;

import ApplicationGUI.ImageManager;
import World.DrawableObject;
import World.Enemies.Enemy;
import World.HealthPoints;
import World.Position;
import World.Towers.Strategy.AttackNearest;
import World.Towers.Strategy.Strategy;
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
    private double Radius = 100;
    private int damage = 4;
    private int speed = 10;
    private Strategy strategy;

    public double getRadius() {
        return Radius;
    }

    public int getSpeed() {
        return speed;
    }

    public int getBulletDamage() {
        return damage;
    }

    public LightUnitTower(Position position, HealthPoints healthPoints, MainTower mainTower) {
        super(position, healthPoints);
        this.mainTower = mainTower;
        strategy = new AttackNearest();
    }


    public void Draw(GraphicsContext gc) {
        Image img = ImageManager.getInstance().getImage(ImageID);
        int image_x = position.getX() - (int)img.getWidth() / 2;
        int image_y = position.getY() - (int)img.getHeight() / 2;

        gc.drawImage(img, image_x, image_y);
    }

    public void Action(ArrayList<DrawableObject> mapObj) {
        strategy.Action(this, mapObj);
    }
}
