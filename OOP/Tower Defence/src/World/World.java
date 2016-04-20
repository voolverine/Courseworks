package World;

import World.Panels.Map;
import World.Panels.ScorePanel;
import World.Panels.ShopPanel;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sun.applet.AppletListener;

import java.util.ArrayList;

/**
 * Created by volverine on 4/19/16.
 */
public class World {
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    private MouseHandler mouseHandler;

    private ArrayList<DrawableObject> mapObjects;
    private ScorePanel scorePanel;
    private ShopPanel shopPanel;
    private Map map;
    private Screen screen;


    public void InitGraphics() {
        gc = canvas.getGraphicsContext2D();
    }


    public void BackgroundRedraw() {
        screen.Draw(gc);
    }


    public void redrawDrawable() {
        for (DrawableObject obj: mapObjects) {
            obj.Draw(gc);
        }
    }


    public void initialize() {
        InitGraphics();
        canvas.setFocusTraversable(true);
        canvas.setHeight(ApplicationGUI.Main.CurrentResolutionH);
        canvas.setWidth(ApplicationGUI.Main.CurrentResolutionW);

        mouseHandler = new MouseHandler(canvas);
        screen = new Screen(new Position(), mouseHandler);
        mapObjects = new ArrayList<DrawableObject> ();
        scorePanel = new ScorePanel();
        shopPanel = new ShopPanel();
        map = new Map();

        mapObjects.add(scorePanel);
        mapObjects.add(shopPanel);
        mapObjects.add(map);


        new AnimationTimer() {
            public void handle(long startNanoTime) {
                BackgroundRedraw();
                redrawDrawable();

                while (System.nanoTime() - startNanoTime < Constants.AVERAGE_TICK) {}
            }
        }.start();
    }
}
