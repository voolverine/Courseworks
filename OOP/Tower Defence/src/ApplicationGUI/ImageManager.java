package ApplicationGUI;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by volverine on 3/21/16.
 */
public class ImageManager {
    private static ImageManager instance = new ImageManager();
    private Map<Integer, Image> Images;

    public static ImageManager getInstance() {
        return instance;
    }

    private void LoadAll() {
//        Images.put(World.Towers.ImageID, new Image("/Images/tower.png"));
//        Images.put(Enemy.ImageID, new Image("/Images/enemy.png"));
//        Images.put(Bullet.ImageID, new Image("/Images/bullet.png"));
    }

    public Image getImage(Integer id) {
        return Images.get(id);
    }

    private ImageManager() {
        Images = new HashMap<Integer, Image>();
        LoadAll();
    }
}