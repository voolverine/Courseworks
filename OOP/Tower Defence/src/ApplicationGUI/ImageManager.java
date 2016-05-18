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
//        Images.put(LightUnitEnemy.ImageID, new Image("/Images/lightEnemy.png"));
        Images.put(LightUnitBullet.ImageID, new Image("/Images/lightUnitBullet.png"));
        Images.put(HeavyUnitTower.ImageID, new Image("/Images/heavyUnitTower.png"));
        Images.put(ProgressBar.ImageID, new Image("/Images/flag.png"));
        Images.put(World.MoneyImageID, new Image("/Images/money.png"));
        Images.put(LevelsController.ImageID, new Image("/Images/levels_background.png"));
        Images.put(Controller.ImageID, new Image("/Images/menu_background.png"));


        Images.put(LightUnitEnemy.down1ID, new Image("/Images/lightUnitEnemy/down1.png"));
        Images.put(LightUnitEnemy.down2ID, new Image("/Images/lightUnitEnemy/down2.png"));
        Images.put(LightUnitEnemy.down3ID, new Image("/Images/lightUnitEnemy/down3.png"));

        Images.put(LightUnitEnemy.up1ID, new Image("/Images/lightUnitEnemy/up1.png"));
        Images.put(LightUnitEnemy.up2ID, new Image("/Images/lightUnitEnemy/up2.png"));
        Images.put(LightUnitEnemy.up3ID, new Image("/Images/lightUnitEnemy/up3.png"));


        Images.put(LightUnitEnemy.left1ID, new Image("/Images/lightUnitEnemy/left1.png"));
        Images.put(LightUnitEnemy.left2ID, new Image("/Images/lightUnitEnemy/left2.png"));
        Images.put(LightUnitEnemy.left3ID, new Image("/Images/lightUnitEnemy/left3.png"));

        Images.put(LightUnitEnemy.right1ID, new Image("/Images/lightUnitEnemy/right1.png"));
        Images.put(LightUnitEnemy.right2ID, new Image("/Images/lightUnitEnemy/right2.png"));
        Images.put(LightUnitEnemy.right3ID, new Image("/Images/lightUnitEnemy/right3.png"));


        Images.put(LightUnitEnemy.left_down1ID, new Image("/Images/lightUnitEnemy/left_down1.png"));
        Images.put(LightUnitEnemy.left_down2ID, new Image("/Images/lightUnitEnemy/left_down2.png"));
        Images.put(LightUnitEnemy.left_down3ID, new Image("/Images/lightUnitEnemy/left_down3.png"));

        Images.put(LightUnitEnemy.left_up1ID, new Image("/Images/lightUnitEnemy/left_up1.png"));
        Images.put(LightUnitEnemy.left_up2ID, new Image("/Images/lightUnitEnemy/left_up2.png"));
        Images.put(LightUnitEnemy.left_up3ID, new Image("/Images/lightUnitEnemy/left_up3.png"));


        Images.put(LightUnitEnemy.right_down1ID, new Image("/Images/lightUnitEnemy/right_down1.png"));
        Images.put(LightUnitEnemy.right_down2ID, new Image("/Images/lightUnitEnemy/right_down2.png"));
        Images.put(LightUnitEnemy.right_down3ID, new Image("/Images/lightUnitEnemy/right_down3.png"));

        Images.put(LightUnitEnemy.right_up1ID, new Image("/Images/lightUnitEnemy/right_up1.png"));
        Images.put(LightUnitEnemy.right_up2ID, new Image("/Images/lightUnitEnemy/right_up2.png"));
        Images.put(LightUnitEnemy.right_up3ID, new Image("/Images/lightUnitEnemy/right_up3.png"));

    }


    public Image getImage(Integer id) {
        return Images.get(id);
    }

    private ImageManager() {
        Images = new HashMap<Integer, Image>();
        LoadAll();
    }
}