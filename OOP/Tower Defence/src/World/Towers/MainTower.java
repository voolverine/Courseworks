package World.Towers;

import ApplicationGUI.ImageManager;
import World.*;
import World.Towers.Strategy.IgnoreEverything;
import World.Towers.Strategy.Reincarnation;
import World.Towers.Strategy.Strategy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * Created by volverine on 5/12/16.
 */
public class MainTower extends Tower implements IHealthDrawable {
    public static Integer ImageID = new Integer(1000);
    HealthProgress healthProgress;
    private Bank bank;
    private int level;
    private int reincarnate_after = 10000;

    private Strategy strategy;


    public int getLevel() {
        return level;
    }

    public MainTower(Position position, HealthPoints healthPoints, Time time, int level) {
        super(position, healthPoints, time);
        healthProgress = new HealthProgress(MainTower.ImageID, healthPoints, position);
        bank = new Bank(1000);
        this.level = level;
        this.strategy = new IgnoreEverything();
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

    public void DrawHealth(GraphicsContext gc) {
        healthProgress.update(gc);
   }

    public void Action(ArrayList<DrawableObject> mapObj) {
        if (World.time.timeGoneAfter(getHealthPoints().getLast_hurtTime(), reincarnate_after)) {
            if (!(strategy instanceof Reincarnation)) {
                strategy = new Reincarnation(this, time);
            }
        } else {
            if (!(strategy instanceof IgnoreEverything)) {
                strategy = new IgnoreEverything();
            }
        }

        strategy.Action(this, mapObj);
    }
}
