package World.Panels;

import ApplicationGUI.Main;
import World.Constants;
import World.Position;
import javafx.application.Application;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Created by volverine on 4/19/16.
 */
public class ScorePanel extends World.DrawableObject {
    private int width;
    private int height;

    /* FXML */
    private GridPane score_panel;
    private StackPane creditsPane;
    private StackPane timePane;
    private StackPane progressPane;


    /* displayed things */
    private Label credits;
    private Label time;
    private Label progress;


    public ScorePanel(GridPane score_panel) {
        super(new World.Position(0, 0));
        this.score_panel = score_panel;
        creditsPane = (StackPane)score_panel.lookup("#credits");
        timePane = (StackPane)score_panel.lookup("#time");
        progressPane = (StackPane)score_panel.lookup("#progress_bar");


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
