package World.Panels;


import World.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by volverine on 4/19/16.
 */
public class ShopPanel extends World.DrawableObject{
    private int width;
    private int height;

    public ShopPanel() {
        super(new World.Position(0,
                    ApplicationGUI.Main.CurrentResolutionH / Constants.SCORE_PANEL_PART));

        int CurrentResolutionH = ApplicationGUI.Main.CurrentResolutionH;
        int CurrentResolutionW = ApplicationGUI.Main.CurrentResolutionW;

        width = CurrentResolutionW / World.Constants.SHOP_PANEL_PART;
        height = CurrentResolutionH - CurrentResolutionH / Constants.SCORE_PANEL_PART;
    }

    public void Draw(GraphicsContext gc) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(position.getX(), position.getY(), width, height);
    }
}
