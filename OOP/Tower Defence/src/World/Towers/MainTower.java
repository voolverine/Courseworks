package World.Towers;

import ApplicationGUI.ImageManager;
import World.HealthPoints;
import World.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Created by volverine on 5/12/16.
 */
public class MainTower extends Tower {
    public static Integer ImageID = new Integer(1000);
    private Bank bank;


    public MainTower(Position position, HealthPoints healthPoints) {
        super(position, healthPoints);
        bank = new Bank();
    }


    public void Draw(GraphicsContext gc) {
        Image img = ImageManager.getInstance().getImage(ImageID);
        int delta_width = (int)img.getWidth() / 2;
        int delta_height = (int)img.getHeight() / 2;

        gc.drawImage(img, this.getPosition().getX(), this.getPosition().getY());
    }
}
