package ApplicationGUI;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import static ApplicationGUI.Main.CurrentResolutionH;
import static ApplicationGUI.Main.CurrentResolutionW;

public class Controller {
    public final static Integer ImageID = new Integer(551);
    public Label play_label;
    public Label open_levels;
    public Label exit_label;

    public Canvas canvas;
    private GraphicsContext gc;

    public void initialize() {
        play_label.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                play_label.setTextFill(Color.DARKORANGE);
            }
        });

        play_label.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                play_label.setTextFill(Color.GREEN);
            }
        });

        open_levels.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                open_levels.setTextFill(Color.DARKORANGE);
            }
        });

        open_levels.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                open_levels.setTextFill(Color.MEDIUMVIOLETRED);
            }
        });

        exit_label.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                exit_label.setTextFill(Color.DARKORANGE);
            }
        });

        exit_label.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                exit_label.setTextFill(Color.BLUEVIOLET);
            }
        });

        exit_label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });


         canvas.setWidth(CurrentResolutionW);
         canvas.setHeight(CurrentResolutionH);
         gc = canvas.getGraphicsContext2D();
         Image img = ImageManager.getInstance().getImage(Controller.ImageID);
         gc.drawImage(img, 0, 0, CurrentResolutionW, CurrentResolutionH);

    }
}
