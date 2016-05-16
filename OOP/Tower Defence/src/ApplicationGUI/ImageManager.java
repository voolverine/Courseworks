package ApplicationGUI;

import World.Bullets.LightUnitBullet;
import World.Enemies.LightUnitEnemy;
import World.ProgressBar;
import World.Towers.HeavyUnitTower;
import World.Towers.LightUnitTower;
import World.Towers.MainTower;
import World.Towers.MoneyTower;
import World.World;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

        Images.put(World.ImageID, new Image("/Images/background.png"));
        Images.put(LightUnitTower.ImageID, new Image("/Images/lightUnitTower.png"));
        Images.put(MoneyTower.ImageID, new Image("/Images/MoneyTower.png"));
        Images.put(MainTower.ImageID, new Image("/Images/mainTower.png"));
        Images.put(LightUnitEnemy.ImageID, new Image("/Images/lightEnemy.png"));
        Images.put(LightUnitBullet.ImageID, new Image("/Images/lightUnitBullet.png"));
        Images.put(HeavyUnitTower.ImageID, new Image("/Images/heavyUnitTower.png"));
        Images.put(ProgressBar.ImageID, new Image("/Images/flag.png"));
    }


    public Image getImage(Integer id) {
        return Images.get(id);
    }

    private ImageManager() {
        Images = new HashMap<Integer, Image>();
        LoadAll();
    }
}