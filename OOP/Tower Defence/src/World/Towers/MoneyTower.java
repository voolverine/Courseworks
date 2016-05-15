package World.Towers;

import ApplicationGUI.ImageManager;
import World.*;
import javafx.animation.Interpolatable;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * Created by volverine on 5/11/16.
 */
public class MoneyTower extends Tower implements IHealthDrawable {
    public static Integer ImageID = new Integer(1001);
    private MainTower mainTower;
    private int salary = 25;
    private long previous_salary_seconds;
    private long salary_period = 30;
    private HealthProgress healthProgress;

    public MoneyTower(Position position, HealthPoints healthPoints, MainTower mainTower, Time time) {
        super(position, healthPoints, time);
        this.mainTower = mainTower;
        previous_salary_seconds = time.getCurrentGameTimeSeconds();
        healthProgress = new HealthProgress(MoneyTower.ImageID, healthPoints, position);
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
        if (time.timeGoneAfter(previous_salary_seconds, salary_period)) {
            mainTower.getBank().invest(salary);
            previous_salary_seconds = time.getCurrentGameTimeSeconds();
        }
    }
}
