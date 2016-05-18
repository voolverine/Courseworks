package World.Barrier;


import ApplicationGUI.ImageManager;
import World.DrawableObject;
import World.Position;
import javafx.scene.image.Image;

/**
 * Created by volverine on 5/16/16.
 */
public class TowerBarier extends DrawableObject {
    private int corner1_x, corner1_y;
    private int corner2_x, corner2_y;

    public TowerBarier(int corner1_x, int corner1_y, int corner2_x, int corner2_y) {
        super(new Position(-1, -1));
        this.corner1_x = corner1_x;
        this.corner1_y = corner1_y;
        this.corner2_x = corner2_x;
        this.corner2_y = corner2_y;
    }

    public boolean canBuild(Position position, Integer ImageID) {
        Image img = ImageManager.getInstance().getImage(ImageID);
        int width = (int)img.getWidth();
        int height = (int)img.getHeight();

        int dx[] = {width / 2, -width / 2, width / 2, -width/ 2};
        int dy[] = {height / 2, -height / 2, -height / 2, height / 2};


        for (int i = 0; i < dx.length; i++) {
            int new_x = position.getX() + dx[i];
            int new_y = position.getY() + dy[i];

            if (corner1_x <= new_x && new_x <= corner2_x &&
                    corner1_y <= new_y && new_y <= corner2_y) {
                return false;
            }
        }

        return true;
    }
}
