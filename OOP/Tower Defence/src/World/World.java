package World;

import ApplicationGUI.ImageManager;
import ApplicationGUI.Main;
import World.Barrier.AverageTree;
import World.Barrier.SmallTree;
import World.Barrier.TowerBarier;
import World.Enemies.Enemy;
import World.Enemies.LightUnitEnemy;
import World.Enemies.Wave;
import World.Towers.*;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * Created by volverine on 4/19/16.
 */
public class World {

    public Label fps_label;
    private Label money_label;
    private ImageView money_pic;
    public Pane pane;
    public Canvas canvas;
    public FlowPane shop_panel;
    public Label time_label;
    public Label reminder;
    public ImageView pause;
    public FlowPane pause_menu;
    public Label level_status;
    public FlowPane money_flow;

    public static Integer MoneyImageID = new Integer(700);

    private GraphicsContext gc;

    private ArrayList<DrawableObject> mapObjects;
    private ArrayList<Wave> waves;
    public static Integer ImageID = new Integer(0);

// gaming variables from here
    public MainTower mainTower;
    public Shop shop;
    private Product product_to_buy = null;
    private MouseHandler mouseHandler;
    private ImageView tobuy_ImageView;
    private ProductGhost productGhost;
    public static Time time;
    private ProgressBar progressBar;
    private boolean paused = false;
    private PauseMenu pauseMenu;
    private FinishLevelChecker finishLevelChecker;
    private Random random;
    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }

    public void InitGraphics() {
        canvas.setFocusTraversable(true);
        canvas.setHeight(ApplicationGUI.Main.CurrentResolutionH);
        canvas.setWidth(ApplicationGUI.Main.CurrentResolutionW);
        gc = canvas.getGraphicsContext2D();

        pauseMenu = new PauseMenu(pause_menu);
        pause.setImage(new Image("/Images/pause.png"));
        pause.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                paused = true;
                pauseMenu.activate();
            }
        });

        pauseMenu.get_continueLabel().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                paused = false;
                pauseMenu.deactivate();
            }
        });


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Creating Shop Panel
        /* Building items */
        tobuy_ImageView = new ImageView();
        tobuy_ImageView.setOpacity(1);
        pane.getChildren().add(tobuy_ImageView);
        shop = new Shop();

        shop.addProduct(new Product("World.Towers.LightUnitTower", 100, LightUnitTower.ImageID));
        shop.addProduct(new Product("World.Towers.MoneyTower", 100, MoneyTower.ImageID));
        shop.addProduct(new Product("World.Towers.HeavyUnitTower", 200, HeavyUnitTower.ImageID));

        for (Product product: shop.getProducts()) {
            shop_panel.getChildren().add(product.getImageView());
            Label price = new Label(String.format("%d", product.getPrice()));
            price.setFont(new Font("Courier New", 11));
            price.setTextFill(Color.GOLD);
            price.setStyle("-fx-font-weight: bold;");
            shop_panel.getChildren().add(price);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Upper Panel
        money_pic = new ImageView(ImageManager.getInstance().getImage(World.MoneyImageID));
        /*
        money_pic.setFitHeight(19);
        money_pic.setFitWidth(32);
        */
        money_label = new Label();
        money_label.setFont(new Font("Courier New", 20));
        money_label.setTextFill(Color.GOLD);
        money_label.setStyle("-fx-font-weight: bold;");
        money_flow.getChildren().add(money_pic);
        money_flow.getChildren().add(money_label);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Setting Z-order

        shop_panel.toFront();
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
            productGhost.Draw(gc);
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
        money_label.setText(String.format("%d", mainTower.getBank().getMoney()));
    }



    public void shopAction() {
        if (product_to_buy == null) {
            product_to_buy = shop.getState();

            if (product_to_buy == null) {
                return;
            } else {
                productGhost = new ProductGhost(new Position(-1, -1), tobuy_ImageView,
                                                    product_to_buy.getImageID(), mouseHandler);
                mouseHandler.getState(); // init mouse state
            }
        }

        productGhost.updatePosition();
        productGhost.updateCanBeBuild(mapObjects);

        if (mainTower.getBank().isEnough(product_to_buy.getPrice())) {
            MouseEvent action = mouseHandler.getState();

            if (action != null && productGhost.canBuild()) {
                int click_x = (int)action.getX();
                int click_y = (int)action.getY();

                    productGhost.setNeed_to_draw(false);
                    productGhost.Disable();
                    productGhost = null;


                    try {
                        Class<?> tower_class = Class.forName(product_to_buy.getClass_name());
                        Constructor<?> construct = tower_class.getConstructor(Position.class, HealthPoints.class,
                                                                                MainTower.class, Time.class);
                        mapObjects.add((DrawableObject)construct.newInstance(new Position(click_x, click_y),
                                                                    new HealthPoints(50), mainTower, time));
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                    mainTower.getBank().buy(product_to_buy.getPrice());
                    product_to_buy = null;
            }
        } else {
            productGhost.setNeed_to_draw(false);
            product_to_buy = null;
        }
    }


    public void Action() {
        int length = mapObjects.size();

        for (int i = 0; i < length; i++) {
            DrawableObject obj = mapObjects.get(i);
            if (obj instanceof IMovable) {
                ((IMovable) obj).Action(mapObjects);
            }
        }

        ArrayList<DrawableObject> to_delete = new ArrayList<DrawableObject>();
        length = mapObjects.size();
        for (int i = 0; i < length; i++) {
            DrawableObject obj = mapObjects.get(i);
            if (obj instanceof IHealthable) {
                if (((IHealthable) obj).getHealthPoints().isKilled()) {
                    to_delete.add(obj);
                }
            }
        }

        for (DrawableObject obj: to_delete) {
            System.out.println(String.format("Removing %s", obj.getClass().toString()));
            mapObjects.remove(obj);
        }
        to_delete.clear();
    }


    public void drawHealth() {
        for (DrawableObject obj: mapObjects) {
            if (obj instanceof IHealthDrawable) {
                ((IHealthDrawable) obj).DrawHealth(gc);
            }
        }
    }

    public void updateWaves() {
        for (int i = 0; i < waves.size(); i++) {
            waves.get(i).updateWave();
        }
    }

    private void generateAverageTrees(int n, int x1, int y1, int x2, int y2) {
        int k = 0;
        int dx = x2 - x1;
        int dy = y2 - y1;

        for (int i = 0; i < 1500; i++) {
            if (k >= n) {
                return;
            }

            Position position = new Position(x1 + random.nextInt(dx), y1 + random.nextInt(dy));
            AverageTree tree = new AverageTree(new Position(-1, -1), new HealthPoints(8));
            if (DrawableObject.isFree(position, tree, mapObjects) &&
                    Position.dist(position, mainTower.getPosition()) >= 250) {

                tree.getPosition().setX(position.getX());
                tree.getPosition().setY(position.getY());

                mapObjects.add(tree);
                k++;
            }
        }
    }

    private void generateSmallTrees(int n, int x1, int y1, int x2, int y2) {
        int k = 0;
        int dx = x2 - x1;
        int dy = y2 - y1;

        for (int i = 0; i < 1500; i++) {
            if (k >= n) {
                return;
            }

            Position position = new Position(x1 + random.nextInt(dx), y1 + random.nextInt(dy));
            SmallTree tree = new SmallTree(new Position(-1, -1), new HealthPoints(4));
            if (DrawableObject.isFree(position, tree, mapObjects) &&
                    Position.dist(position, mainTower.getPosition()) >= 250) {

                tree.getPosition().setX(position.getX());
                tree.getPosition().setY(position.getY());

                mapObjects.add(tree);
                k++;
            }
        }
    }

    private void generateTrees(int n, int x1, int y1, int x2, int y2) {
        int smallTrees = random.nextInt(n);
        int averageTrees = n - smallTrees;
        generateSmallTrees(smallTrees, x1, y1, x2, y2);
        generateAverageTrees(averageTrees, x1, y1, x2, y2);
    }

    void load_level1() {
        mainTower = new MainTower(new Position(600, 350), new HealthPoints(80), time, 1);
        mapObjects.add(mainTower);
        mapObjects.add(new TowerBarier(-500, -500, Main.CurrentResolutionW, 100));
        mapObjects.add(new TowerBarier(-500, 500, 100, Main.CurrentResolutionH));
        mapObjects.add(new TowerBarier(100, Main.CurrentResolutionH - 100, Main.CurrentResolutionW, Main.CurrentResolutionH));
        mapObjects.add(new TowerBarier(Main.CurrentResolutionW - 100, 0, Main.CurrentResolutionW, Main.CurrentResolutionH));

        waves.add(new Wave(mapObjects, time, new ArrayList<>(Arrays.asList(
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(500, 1000), new HealthPoints(40), mainTower, time),
                    new Long(1000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(200, 1000), new HealthPoints(40), mainTower, time),
                    new Long(20000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(200, 1000), new HealthPoints(40), mainTower, time),
                    new Long(40000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(700, 1000), new HealthPoints(40), mainTower, time),
                    new Long(60000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(900, 1000), new HealthPoints(40), mainTower, time),
                    new Long(60000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(800, 1000), new HealthPoints(40), mainTower, time),
                    new Long(60000))
        )), 60000));

        waves.add(new Wave(mapObjects, time, new ArrayList<>(Arrays.asList(
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(1000, 1000), new HealthPoints(40), mainTower, time),
                    new Long(70000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(1100, 1200), new HealthPoints(40), mainTower, time),
                    new Long(80000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(10, 800), new HealthPoints(40), mainTower, time),
                    new Long(80000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(500, 900), new HealthPoints(40), mainTower, time),
                    new Long(90000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(330, 800), new HealthPoints(40), mainTower, time),
                    new Long(100000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(900, 900), new HealthPoints(40), mainTower, time),
                    new Long(120000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(500, 840), new HealthPoints(40), mainTower, time),
                    new Long(120000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(700, 840), new HealthPoints(40), mainTower, time),
                    new Long(121000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(1000, 800), new HealthPoints(40), mainTower, time),
                    new Long(121000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(800, 1000), new HealthPoints(40), mainTower, time),
                    new Long(121000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(100, 900), new HealthPoints(40), mainTower, time),
                    new Long(122000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(-50, 900), new HealthPoints(40), mainTower, time),
                    new Long(122000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(-20, 800), new HealthPoints(40), mainTower, time),
                    new Long(122000))
        )), 120000));

        generateTrees(35, 0, 0, Main.CurrentResolutionW, 200);
        generateTrees(15, 0, 0, 200, Main.CurrentResolutionH);
        generateTrees(15, Main.CurrentResolutionW - 200, 0, Main.CurrentResolutionW, Main.CurrentResolutionH);

        progressBar = new ProgressBar(time, waves, reminder);
        finishLevelChecker = new FinishLevelChecker(mainTower, mapObjects, time, waves, level_status);
    }

    public void load_level2() {
        mainTower = new MainTower(new Position(600, 350), new HealthPoints(80), time, 1);
        mapObjects.add(mainTower);
        mapObjects.add(new TowerBarier(-500, -500, Main.CurrentResolutionW, 50));
        mapObjects.add(new TowerBarier(-500, 500, 100, Main.CurrentResolutionH));
        mapObjects.add(new TowerBarier(100, Main.CurrentResolutionH - 100, Main.CurrentResolutionW, Main.CurrentResolutionH));
        mapObjects.add(new TowerBarier(Main.CurrentResolutionW - 100, 0, Main.CurrentResolutionW, Main.CurrentResolutionH));

        waves.add(new Wave(mapObjects, time, new ArrayList<>(Arrays.asList(
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(1000, 1200), new HealthPoints(40), mainTower, time),
                    new Long(1000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(500, 1200), new HealthPoints(40), mainTower, time),
                    new Long(20000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(-10, 700), new HealthPoints(40), mainTower, time),
                    new Long(40000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(-10, 700), new HealthPoints(40), mainTower, time),
                    new Long(60000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(-50, 700), new HealthPoints(40), mainTower, time),
                    new Long(60000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(800, -40), new HealthPoints(40), mainTower, time),
                    new Long(60000))
        )), 60000));

        waves.add(new Wave(mapObjects, time, new ArrayList<>(Arrays.asList(
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(1000, 1200), new HealthPoints(40), mainTower, time),
                    new Long(70000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(1300, 500), new HealthPoints(40), mainTower, time),
                    new Long(80000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(-10, 800), new HealthPoints(40), mainTower, time),
                    new Long(80000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(600, 900), new HealthPoints(40), mainTower, time),
                    new Long(90000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(-50, 700), new HealthPoints(40), mainTower, time),
                    new Long(100000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(900, 900), new HealthPoints(40), mainTower, time),
                    new Long(120000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(500, -40), new HealthPoints(40), mainTower, time),
                    new Long(120000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(700, -40), new HealthPoints(40), mainTower, time),
                    new Long(121000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(1300, 400), new HealthPoints(40), mainTower, time),
                    new Long(121000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(0, 0), new HealthPoints(40), mainTower, time),
                    new Long(121000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(1300, 500), new HealthPoints(40), mainTower, time),
                    new Long(122000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(500, -50), new HealthPoints(40), mainTower, time),
                    new Long(122000)),
            new Pair<Enemy, Long>(new LightUnitEnemy(new Position(-20, 500), new HealthPoints(40), mainTower, time),
                    new Long(122000))
        )), 120000));


        generateTrees(15, 0, 0, Main.CurrentResolutionW, Main.CurrentResolutionH);

        progressBar = new ProgressBar(time, waves, reminder);
        finishLevelChecker = new FinishLevelChecker(mainTower, mapObjects, time, waves, level_status);
    }

    public void initialize() {
        InitGraphics();

        random = new Random();
        time = new Time(time_label);
        mouseHandler = new MouseHandler(pane);
        mapObjects = new ArrayList<DrawableObject> ();
        waves = new ArrayList<Wave>();

        if (Main.level_to_play == 1) {
            load_level1();
        } else
        if (Main.level_to_play == 2) {
            load_level2();
        }

        mapObjects.add(time);


        new AnimationTimer() {
            public void handle(long startNanoTime) {
                String status = finishLevelChecker.updateGameStatus();
                if (!paused && status == "GAMING") {
                    updateWaves();
                    Action();
                    BackgroundRedraw();
                    redrawDrawable();
                    updateMoney();


                    shopAction();
                    drawHealth();
                    progressBar.update(gc);
                }
                /*
                FPS += 1;
                fps_nanoTimer_current = System.nanoTime();
                if (Math.abs(fps_nanoTimer_start - fps_nanoTimer_current) >= 1000000000.0) {
                    updateFPS();
                }
                */
            }
        }.start();
    }
}