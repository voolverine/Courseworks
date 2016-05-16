package ApplicationGUI;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Controller {
    public Label play_label;
    public Label open_levels;
    public Label exit_label;

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
    }
}
