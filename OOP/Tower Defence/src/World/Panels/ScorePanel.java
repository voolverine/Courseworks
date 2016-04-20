package World.Panels;

import ApplicationGUI.Main;
import World.Constants;
import World.Position;
import javafx.application.Application;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by volverine on 4/19/16.
 */
public class ScorePanel extends World.DrawableObject {
    private int width;
    private int height;

    public ScorePanel() {
        super(new World.Position(0, 0));

        int CurrentResolutionH = ApplicationGUI.Main.CurrentResolutionH;
        int CurrentResolutionW = ApplicationGUI.Main.CurrentResolutionW;

        width = CurrentResolutionW;
        height = CurrentResolutionH / World.Constants.SCORE_PANEL_PART;
    }

    public void Draw(GraphicsContext gc) {
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(position.getX(), position.getY(), width, height);
    }
}
