package World;

import ApplicationGUI.ImageManager;
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
    public Label fps;
    public Pane pane;
    public Canvas canvas;

    private GraphicsContext gc;

    private MouseHandler mouseHandler;

    private ArrayList<DrawableObject> mapObjects;


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


    private double FPS = 0;
    private long fps_nanoTimer_start = 0;
    private long fps_nanoTimer_current = 0;

    public void updateFPS() {
        fps.setText(String.format("fps = %f", FPS));
        FPS = 0;

        fps_nanoTimer_start = System.nanoTime();
    }

    public void initialize() {
        InitGraphics();

        mouseHandler = new MouseHandler(pane);
        mapObjects = new ArrayList<DrawableObject> ();

        new AnimationTimer() {
            public void handle(long startNanoTime) {
                BackgroundRedraw();
                redrawDrawable();


                FPS += 1;
                fps_nanoTimer_current = System.nanoTime();
                if (Math.abs(fps_nanoTimer_start - fps_nanoTimer_current) >= 1000000000.0) {
                    updateFPS();
                }
            }
        }.start();
    }
}
