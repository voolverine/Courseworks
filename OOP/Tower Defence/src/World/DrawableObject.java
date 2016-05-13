package World;


import ApplicationGUI.ImageManager;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.lang.reflect.Field;

/**
 * Created by volverine on 4/19/16.
 */
public class DrawableObject {
    protected Position position;
    protected int img_width = -1;
    protected int img_height = -1;


    public Position getPosition() {
        return position;
    }

    public DrawableObject(Position position) {
        this.position = position;
    }


    public static boolean isIntersects(DrawableObject obj1, DrawableObject obj2) {
        return tryIntersects(obj1, obj2) || tryIntersects(obj2, obj1);
    }


    public static boolean tryIntersects(DrawableObject obj1, DrawableObject obj2) {
        try {
            Field field1 = obj1.getClass().getField("ImageID");
            Field field2 = obj1.getClass().getField("ImageID");

            if (field1 != null && field2 != null) {
                Integer img_id1 = (Integer)field1.get(obj1);
                Integer img_id2 = (Integer)field2.get(obj2);

                Image image1 = ImageManager.getInstance().getImage(img_id1);
                Image image2 = ImageManager.getInstance().getImage(img_id2);

                int width1 = (int)image1.getWidth();
                int height1 = (int)image1.getHeight();
                int width2 = (int)image2.getWidth();
                int height2 = (int)image2.getHeight();


                int dx[] = {width1, width1, 0, 0};
                int dy[] = {0, height1, height1, 0};

                // Normalized image corners
                int x1 = obj2.getPosition().getX() - width2 / 2;
                int x2 = obj2.getPosition().getX() + width2 / 2;
                int y1 = obj2.getPosition().getY() - height2 / 2;
                int y2 = obj2.getPosition().getY() + height2 / 2;

                // Normalize obj1 image corners
                int cur_x = obj1.getPosition().getX() - width1 / 2;
                int cur_y = obj1.getPosition().getY() - height1 / 2;


                for (int i = 0; i < dx.length; i++) {
                    int newx = cur_x + dx[i];
                    int newy = cur_y + dy[i];
                    if (x1 <= newx && newx <= x2 &&
                            y1 <= newy && newy <= y2) {
                        return true;
                    }
                }
                // if we are still here objects don't intersect
                return false;
            } else {
                return false;
            }
        } catch(Exception e) {
            return false;
        }
    }


    public void Draw(GraphicsContext gc) {}
    public void Action() {}
}
