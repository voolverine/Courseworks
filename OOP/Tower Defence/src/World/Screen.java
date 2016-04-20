package World;

import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import sun.management.snmp.jvmmib.JvmRTInputArgsEntryMBean;

import java.util.ArrayList;

/**
 * Created by volverine on 4/20/16.
 */
public class Screen {
    public static final Integer ImageID = new Integer(1000);

    private Position position;
    private int map_width;
    private int map_height;
    private int screen_width;
    private int screen_height;
    private MouseHandler mouseHandler;

    private static int step = 15;

    public Screen(Position position, MouseHandler mouseHandler) {
        this.position = position;
        Image map = ApplicationGUI.ImageManager.getInstance().getImage(ImageID);
        this.map_width = (int) map.getWidth();
        this.map_height = (int) map.getHeight();
        this.screen_height = ApplicationGUI.Main.CurrentResolutionH;
        this.screen_width = ApplicationGUI.Main.CurrentResolutionW;
        this.mouseHandler = mouseHandler;
    }


    private void moveUp() {
        int new_y = position.getY() - step;
        if (new_y >= 0 && new_y + screen_height < map_height) {
            position.moveUp(step);
        }
    }
    private void moveDown() {
        System.out.println(position.getY());
        System.out.println(screen_height);

        int new_y = position.getY() + step;
        if (new_y >= 0 && new_y + screen_height < map_height) {
            position.moveDown(step);
        }
    }

    private void moveLeft() {
        int new_x = position.getX() - step;
        if (new_x >= 0 && new_x + screen_width < map_width) {
            position.moveLeft(step);
        }
    }

    private void moveRight() {
        int new_x = position.getX() + step;
        if (new_x >= 0 && new_x + screen_width < map_width) {
            position.moveRight(step);
        }
    }


    private void updateState() {
        ArrayList<String> response = mouseHandler.getCurrentState();

        for (String action: response) {
            if (action == "MOVE_SCREEN_UP") {
                moveUp();
            } else
            if (action == "MOVE_SCREEN_DOWN") {
                moveDown();
            } else
            if (action == "MOVE_SCREEN_LEFT") {
                moveLeft();
            } else
            if (action == "MOVE_SCREEN_RIGHT") {
                moveRight();
            }
        }
    }

    public void Draw(GraphicsContext gc) {
        updateState();
        Image world = ApplicationGUI.ImageManager.getInstance().getImage(ImageID);

        WritableImage croppedWorld = new WritableImage(world.getPixelReader(),
                                                        position.getX(), position.getY(),
                                                        screen_width, screen_height);
        gc.drawImage(croppedWorld, 0, 0);
    }
}
