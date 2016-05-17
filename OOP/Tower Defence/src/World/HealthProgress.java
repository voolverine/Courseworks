package World;

import ApplicationGUI.ImageManager;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Created by volverine on 5/14/16.
 */
public class HealthProgress {
    private int image_width;
    private int image_height;
    private HealthPoints healthPoints;
    private Position position;

    private int bar_width;
    private int bar_height;
    private static int margin = 1;
    private int inner_width;
    private int inner_height;

    private int corner_x;
    private int corner_y;

    public HealthProgress(Integer ImageID, HealthPoints healthPoints, Position position) {
        Image img = ImageManager.getInstance().getImage(ImageID);
        image_height = (int)img.getHeight();
        image_width = (int)img.getWidth();

        this.healthPoints = healthPoints;
        this.position = position;

        bar_width = image_width;
        bar_height = Math.max(image_height / 9, 7);
        inner_width = bar_width - HealthProgress.margin * 2;
        inner_height = bar_height - HealthProgress.margin * 2;
    }

    public void Draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(corner_x, corner_y, bar_width, bar_height);

        double greenWidth = ((double)healthPoints.get_percentageHP()) / 100.0 * (double)inner_width;

        int green_width = (int)greenWidth;
        int red_width = inner_width - green_width;

        int green_begins_x = corner_x + margin;
        int green_begins_y = corner_y + margin;

        gc.setFill(Color.GREEN);
        gc.fillRect(green_begins_x, green_begins_y, green_width, inner_height);

        int red_begins_x = green_begins_x + green_width;
        int red_begins_y = corner_y + margin;

        gc.setFill(Color.RED);
        gc.fillRect(red_begins_x, red_begins_y, red_width, inner_height);
    }

    public void updatePostition() {
        corner_x = position.getX() - image_width / 2;
        corner_y = position.getY() - image_height / 2 - bar_height - 4;
    }


    public void update(GraphicsContext gc) {
        updatePostition();
        Draw(gc);
    }

}
