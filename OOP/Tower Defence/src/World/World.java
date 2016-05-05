package World;

import World.Panels.Map;
import World.Panels.ScorePanel;
import World.Panels.ShopPanel;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.runtime.SystemProperties;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sun.applet.AppletListener;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by volverine on 4/19/16.
 */
public class World {
    @FXML
    public ImageView imageView;
    @FXML
    public Rectangle map_focus;
    @FXML
    public Pane pane;
    @FXML
    public GridPane score_panel;
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

    public static double FPS = 0;
    long afterPrevFPS = 0;
    long StrartNanoFPS = 0;
    long curNanoFPS = 0;

    public void showFPS() {
        Label l = (Label)score_panel.lookup("#gamefps");
        l.setText(String.format("fps = %f", FPS));
        FPS = 0;
        StrartNanoFPS = System.nanoTime();
    }

    public void initialize() {
        InitGraphics();
        canvas.setFocusTraversable(true);
        canvas.setHeight(ApplicationGUI.Main.CurrentResolutionH);
        canvas.setWidth(ApplicationGUI.Main.CurrentResolutionW);

        mouseHandler = new MouseHandler(pane);
        screen = new Screen(new Position(), mouseHandler);
        map = new Map(imageView, screen, map_focus);
        mapObjects = new ArrayList<DrawableObject> ();
        scorePanel = new ScorePanel(score_panel);
        shopPanel = new ShopPanel();

        mapObjects.add(scorePanel);
        mapObjects.add(shopPanel);
        mapObjects.add(map);/*
        Properties prop = System.getProperties();
        prop.setProperty("javafx.animation.fullspeed", "true");
*/
        new AnimationTimer() {
            public void handle(long startNanoTime) {
                BackgroundRedraw();
                redrawDrawable();

               // while (System.nanoTime() - startNanoTime < Constants.AVERAGE_TICK) {}
                FPS += 1;
                curNanoFPS = System.nanoTime();
                if (Math.abs(StrartNanoFPS - curNanoFPS) >= 1000000000.0) {
                    showFPS();
                }
            }
        }.start();
    }
}
