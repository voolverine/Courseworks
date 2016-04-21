package World.Panels;

import ApplicationGUI.Main;
import World.Constants;
import World.Position;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * Created by volverine on 4/19/16.
 */
public class Map extends World.DrawableObject {
/* PANEL FIELDS */
    private int width;
    private int height;
    private double kx;
    private double ky;
    private int panelBorder = 4;
/* */

/* FRAME FIELDS */
    private double frameWidth, frameHeight;
/* */

    private World.Screen screen;
    private ImageView imageView;
    private Rectangle frame;


    private void setFramePos() {
        int x = screen.getPosition().getX();
        int y = screen.getPosition().getY();

        double new_x = (double)x * kx;
        double new_y = (double)y * ky;

        frame.setX(position.getX() + (int)new_x + 1);
        frame.setY(position.getY() + (int)new_y + 1);
    }


    private void initFrame() {
        frame.setStroke(Color.YELLOW);
        frame.setStrokeWidth(2.0);
        frame.setFill(Color.TRANSPARENT);

        frameWidth = (double)screen.getWidth() / (double)screen.getMap_width() * (double)width;
        frameHeight = (double)screen.getHeight() / (double)screen.getMap_height() * (double)height;
        frame.setWidth(frameWidth);
        frame.setHeight(frameHeight);
    }


    private void initImageView() {
        imageView.setX(position.getX());
        imageView.setY(position.getY());
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        imageView.setPreserveRatio(true);
    }

    public Map(ImageView imageView, World.Screen screen, Rectangle frame) {
        super(new World.Position(0, 0)); // Pass this

        this.screen = screen;
        width = Math.min(ApplicationGUI.Main.CurrentResolutionH, ApplicationGUI.Main.CurrentResolutionW) / Constants.MAP_PART;
        height = width;

        position = new World.Position(ApplicationGUI.Main.CurrentResolutionW - width,
                                        ApplicationGUI.Main.CurrentResolutionH - height);


        kx = (double)width / (double)screen.getMap_width();
        ky = (double)height / (double)screen.getMap_height();

        this.imageView = imageView;
        initImageView();

        this.frame = frame;
        initFrame();
        setFramePos();
    }


    public void drawBorder(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(panelBorder);

        int x1 = position.getX();
        int y1 = position.getY();

        int x2 = x1 + width;
        int y2 = y1 + height;
        gc.strokeLine(x1, y1, x1, y2);
        gc.strokeLine(x1, y1, x2, y1);
        gc.strokeLine(x1, y2, x2, y2);
        gc.strokeLine(x2, y1, x2, y2);
    }

    public void Draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        drawBorder(gc);
        Image image = ApplicationGUI.ImageManager.getInstance().getImage(screen.ImageID);
        imageView.setImage(image);
        setFramePos();
    }
}
