package World;

import ApplicationGUI.ImageManager;
import World.Towers.LightUnitTower;
import World.Towers.MainTower;
import World.Towers.MoneyTower;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import java.util.ArrayList;


/**
 * Created by volverine on 4/19/16.
 */
public class World {
    public Label fps_label;
    public Label money_label;
    public Pane pane;
    public Canvas canvas;

    private GraphicsContext gc;

    private MouseHandler mouseHandler;

    private ArrayList<DrawableObject> mapObjects;


// gaming variables from here
    MainTower mainTower;


    public void InitGraphics() {
        canvas.setFocusTraversable(true);
        canvas.setHeight(ApplicationGUI.Main.CurrentResolutionH);
        canvas.setWidth(ApplicationGUI.Main.CurrentResolutionW);
        gc = canvas.getGraphicsContext2D();
    }


    public void BackgroundRedraw() {
        Integer mapID = Constants.Lvl1ID;
        Image img = ImageManager.getInstance().getImage(mapID);
        gc.drawImage(img, 0, 0);
    }


    public void redrawDrawable() {
        for (DrawableObject obj: mapObjects) {
            obj.Draw(gc);
        }
    }


    public void Action() {

    }


    private double FPS = 0;
    private long fps_nanoTimer_start = 0;
    private long fps_nanoTimer_current = 0;

    public void updateFPS() {
        fps_label.setText(String.format("fps = %f", FPS));
        FPS = 0;

        fps_nanoTimer_start = System.nanoTime();
    }

    public void updateMoney() {
        money_label.setText(String.format("Money = "));
    }


    public void initialize() {
        InitGraphics();

        mouseHandler = new MouseHandler(pane);
        mapObjects = new ArrayList<DrawableObject> ();
        mainTower = new MainTower(new Position(100, 100), new HealthPoints(1000));

        mapObjects.add(mainTower);
        mapObjects.add(new LightUnitTower(new Position(50, 50), new HealthPoints(500)));
        mapObjects.add(new MoneyTower(new Position(1200, 150), new HealthPoints(200)));

        new AnimationTimer() {
            public void handle(long startNanoTime) {
                BackgroundRedraw();
                redrawDrawable();
                Action();
                updateMoney();


                FPS += 1;
                fps_nanoTimer_current = System.nanoTime();
                if (Math.abs(fps_nanoTimer_start - fps_nanoTimer_current) >= 1000000000.0) {
                    updateFPS();
                }
            }
        }.start();
    }
}