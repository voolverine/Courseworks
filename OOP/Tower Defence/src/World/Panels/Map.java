package World.Panels;

import ApplicationGUI.Main;
import World.Constants;
import World.Position;
import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by volverine on 4/19/16.
 */
public class Map extends World.DrawableObject {
    private int width;
    private int height;

    public Map() {
        super(new World.Position(0, 0)); // Pass this

        width = Math.min(ApplicationGUI.Main.CurrentResolutionH, ApplicationGUI.Main.CurrentResolutionW) / Constants.MAP_PART;
        height = width;

        position = new World.Position(ApplicationGUI.Main.CurrentResolutionW - width,
                                        ApplicationGUI.Main.CurrentResolutionH - height);

    }

    public void Draw(GraphicsContext gc) {
        gc.setFill(Color.PALETURQUOISE);
        gc.fillRect(position.getX(), position.getY(), width, height);
    }
}
