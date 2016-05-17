package World;

import com.sun.tools.javac.comp.Flow;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

/**
 * Created by volverine on 5/16/16.
 */
public class PauseMenu {
    private FlowPane flowPane;
    private Label Continue;
    private Label exitToMenu;

    public Label get_continueLabel() {
        return Continue;
    }

    public Label get_ExitToMenuLabel() {
        return exitToMenu;
    }

    private void set_continue_style() {
        Continue.setText("          Continue");
        Continue.setStyle("-fx-font-size: 20px;");
    }

    private void set_exit_style() {
        exitToMenu.setText("          Menu");
        exitToMenu.setStyle("-fx-font-size: 20px;");
    }

    public void activate() {
        flowPane.setOpacity(1);
        flowPane.setDisable(false);
    }

    public void deactivate() {
        flowPane.setOpacity(0);
        flowPane.setDisable(true);
    }

    public PauseMenu(FlowPane flowpane) {
        this.flowPane = flowpane;
        Continue = new Label();
        exitToMenu = new Label();
        Continue.setTextFill(Color.DARKGOLDENROD);
        exitToMenu.setTextFill(Color.DARKGOLDENROD);

        Continue.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Continue.setTextFill(Color.DARKORANGE);
            }
        });

        Continue.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Continue.setTextFill(Color.DARKGOLDENROD);
            }
        });

        exitToMenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                exitToMenu.setTextFill(Color.DARKORANGE);
            }
        });

        exitToMenu.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                exitToMenu.setTextFill(Color.DARKGOLDENROD);
            }
        });

        flowpane.setOpacity(0);
        flowpane.getChildren().add(Continue);
        flowpane.getChildren().add(exitToMenu);
        set_continue_style();
        set_exit_style();
    }
}
