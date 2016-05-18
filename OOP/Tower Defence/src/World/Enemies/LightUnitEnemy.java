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
    public static Integer ImageID = new Integer(1500);
    public final static Integer up1ID = new Integer(1500);
    public final static Integer up2ID = new Integer(1501);
    public final static Integer up3ID = new Integer(1502);

    public final static Integer left1ID = new Integer(1503);
    public final static Integer left2ID = new Integer(1504);
    public final static Integer left3ID = new Integer(1505);

    public final static Integer right1ID = new Integer(1506);
    public final static Integer right2ID = new Integer(1507);
    public final static Integer right3ID = new Integer(1508);

    public final static Integer down1ID = new Integer(1509);
    public final static Integer down2ID = new Integer(1510);
    public final static Integer down3ID = new Integer(1511);

    public final static Integer left_down1ID = new Integer(1512);
    public final static Integer left_down2ID = new Integer(1513);
    public final static Integer left_down3ID = new Integer(1514);

    public final static Integer left_up1ID = new Integer(1515);
    public final static Integer left_up2ID = new Integer(1516);
    public final static Integer left_up3ID = new Integer(1517);

    public final static Integer right_down1ID = new Integer(1518);
    public final static Integer right_down2ID = new Integer(1519);
    public final static Integer right_down3ID = new Integer(1520);

    public final static Integer right_up1ID = new Integer(1521);
    public final static Integer right_up2ID = new Integer(1522);
    public final static Integer right_up3ID = new Integer(1523);


    private HealthProgress healthProgress;
    Strategy forwardStrategy;
    Strategy attackStrategy;
    private int damage = 5;
    private int Radius = 150;
    private int atackSpeed = 1000;
    private int movingSpeed = 30;

    private int afterImageChange = 0;
    private int imageChangeSpeed = 10;

    public int getBulletDamage() {
        return damage;
    }

    public double getRadius() {
        return Radius;
    }

    public int getAtackSpeed() {
        return atackSpeed;
    }

    public int getMovingSpeed() {
        return movingSpeed;
    }


    public void setPosition(int new_x, int new_y) {

        int prev_x = position.getX();
        int prev_y = position.getY();

        position.setX(new_x);
        position.setY(new_y);

        afterImageChange++;
        if (afterImageChange < imageChangeSpeed) {
            return;
        }
        afterImageChange = 0;

        int dx = prev_x - position.getX();
        int dy = prev_y - position.getY();
        int cur = ImageID;

         if (dy > 0) {
            if (cur >= up1ID && cur <= up3ID) {
                if (cur == up1ID) {
                    ImageID = up2ID;
                } else
                if (cur == up2ID) {
                    ImageID = up3ID;
                } else {
                   ImageID = up1ID;
                }
           } else {
               ImageID = up1ID;
           }
        } else
        if (dy <= 0) {
            if (cur >= down1ID && cur <= down3ID) {
                if (cur == down1ID) {
                    ImageID = down2ID;
                } else
                if (cur == down2ID) {
                    ImageID = down3ID;
                } else {
                   ImageID = down1ID;
                }
           } else {
               ImageID = down1ID;
           }
        }


    }


    public LightUnitEnemy(Position position, HealthPoints healthPoints, MainTower mainTower, Time time) {
        super(position, healthPoints, mainTower, time);
        healthProgress = new HealthProgress(LightUnitEnemy.ImageID, healthPoints, position);
        forwardStrategy = new ForwardMainTower(this, time);
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
