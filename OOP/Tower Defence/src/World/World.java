package World;

import ApplicationGUI.ImageManager;
import ApplicationGUI.Main;
import World.Enemies.Enemy;
import World.Enemies.LightUnitEnemy;
import World.Towers.LightUnitTower;
import World.Towers.MainTower;
import World.Towers.MoneyTower;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;


/**
 * Created by volverine on 4/19/16.
 */
public class World {

    public Label fps_label;
    public Label money_label;
    public Pane pane;
    public Canvas canvas;
    public FlowPane shop_panel;

    private GraphicsContext gc;

    private ArrayList<DrawableObject> mapObjects;
    public static Integer ImageID = new Integer(0);

// gaming variables from here
    public MainTower mainTower;
    public Shop shop;
    private Product product_to_buy = null;
    private MouseHandler mouseHandler;
    private ImageView tobuy_ImageView;
    private ProductGhost productGhost;


    public void InitGraphics() {
        canvas.setFocusTraversable(true);
        canvas.setHeight(ApplicationGUI.Main.CurrentResolutionH);
        canvas.setWidth(ApplicationGUI.Main.CurrentResolutionW);
        gc = canvas.getGraphicsContext2D();

        tobuy_ImageView = new ImageView();
        tobuy_ImageView.setOpacity(1);
        pane.getChildren().add(tobuy_ImageView);
        shop = new Shop();

        shop.addProduct(new Product("LightUnitTower", 100, LightUnitTower.ImageID));
        shop.addProduct(new Product("MoneyTower", 100, MoneyTower.ImageID));

        for (Product product: shop.getProducts()) {
            shop_panel.getChildren().add(product.getImageView());
        }
    }


    public void BackgroundRedraw() {
        Image img = ImageManager.getInstance().getImage(ImageID);
        gc.drawImage(img, 0, 0, Main.CurrentResolutionW, Main.CurrentResolutionH);
    }


    public void redrawDrawable() {
        for (DrawableObject obj: mapObjects) {
            obj.Draw(gc);
        }

        if (productGhost != null) {
            productGhost.Draw();
        }
    }


    private double FPS = 0;
    private long fps_nanoTimer_start = 0;
    private long fps_nanoTimer_current = 0;

    public void updateFPS() {
        fps_label.setText(String.format("fps = %.2f", FPS));
        FPS = 0;

        fps_nanoTimer_start = System.nanoTime();
    }

    public void updateMoney() {
        money_label.setText(String.format("Money = "));
    }


    public boolean isFree(int x, int y) {
        /* TODO */
        return false;
    }


    public void checkShop() {
        if (product_to_buy == null) {
            product_to_buy = shop.getState();

            if (product_to_buy == null) {
                return;
            } else {
                productGhost = new ProductGhost(tobuy_ImageView, product_to_buy.getImageID(), mouseHandler);
            }
        }

        if (mainTower.getBank().isEnough(product_to_buy.getPrice())) {
            MouseEvent action = mouseHandler.getState();

            if (action != null) {
                /* TODO */
                int click_x = (int)action.getX();
                int click_y = (int)action.getY();

                if (isFree(click_x, click_y)) {
                    productGhost.setNeed_to_draw(false);
                    product_to_buy = null;
                } else {
                    /* TODO */
                }
            }
        } else {
            product_to_buy = null;
        }
    }


    public void initialize() {
        InitGraphics();

        mouseHandler = new MouseHandler(pane);
        mapObjects = new ArrayList<DrawableObject> ();
        mainTower = new MainTower(new Position(100, 100), new HealthPoints(1000));

        mapObjects.add(mainTower);
        mapObjects.add(new LightUnitTower(new Position(50, 50), new HealthPoints(500)));
        mapObjects.add(new MoneyTower(new Position(1200, 150), new HealthPoints(200)));
        mapObjects.add(new LightUnitEnemy(new Position(400, 400), new HealthPoints(400)));

        new AnimationTimer() {
            public void handle(long startNanoTime) {
                BackgroundRedraw();
                redrawDrawable();
                updateMoney();


                checkShop();
                FPS += 1;
                fps_nanoTimer_current = System.nanoTime();
                if (Math.abs(fps_nanoTimer_start - fps_nanoTimer_current) >= 1000000000.0) {
                    updateFPS();
                }
            }
        }.start();
    }
}