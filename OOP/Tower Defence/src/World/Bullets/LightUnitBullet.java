package World.Bullets;

import ApplicationGUI.ImageManager;
import World.DrawableObject;
import World.HealthPoints;
import World.IHealthable;
import World.Position;
import World.Towers.LightUnitTower;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * Created by volverine on 5/13/16.
 */
public class LightUnitBullet extends Bullet {
    public static Integer ImageID = new Integer(5000);

    private static int dx[] = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static int dy[] = {-1, 0, 1, 0, 0, -1, 0, 1};

    public LightUnitBullet(Position position, DrawableObject target, int damage) {
        super(position, target, damage);
        healthPoints = new HealthPoints(1);
    }

    private void setPosition(int new_x, int new_y) {
        position.setX(new_x);
        position.setY(new_y);
    }

    public void Draw(GraphicsContext gc) {
        if (healthPoints.isKilled()) {
            return;
        }

        Image img = ImageManager.getInstance().getImage(LightUnitBullet.ImageID);
        int image_x = position.getX() - (int)img.getWidth() / 2;
        int image_y = position.getY() - (int)img.getHeight() / 2;

        gc.drawImage(img, image_x, image_y);

    }

    public void Action(ArrayList<DrawableObject> mapObj) {
        double dist = 999999;
        int best = 0;

        int cur_x = position.getX();
        int cur_y = position.getY();

        for (int i = 0; i < dx.length; i++) {
            int new_x = cur_x + dx[i];
            int new_y = cur_y + dy[i];
            double new_dist = Position.dist(new Position(new_x, new_y), target.getPosition());

            if (new_dist < dist) {
                dist = new_dist;
                best = i;
            }
        }

        setPosition(cur_x + dx[best], cur_y + dy[best]);
        if (Position.dist(this, target) < 1.5) {
            ((IHealthable) target).getHealthPoints().hurt(damage);
            healthPoints.hurt(1);
        }
        /*
        int x1 = position.getX();
        int y1 = position.getY();

        int x2 = target.getPosition().getX();
        int y2 = target.getPosition().getY();

        int A = x2 - y1;
        int B = x1 - x2;
        int C = x2 * y1 - x1 * y2;

        */
    }
}
