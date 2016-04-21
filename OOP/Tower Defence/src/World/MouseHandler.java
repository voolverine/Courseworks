package World;

import ApplicationGUI.Main;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;


/**
 * Created by volverine on 3/23/16.
 */
public class MouseHandler {
    private MouseEvent action;
    private Position position = new Position(1, 1);


    public MouseHandler(Pane pane) {
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                action = mouseEvent;
            }
        });

        pane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                position.setX((int) event.getX());
                position.setY((int) event.getY());
            }
        });
    }

    public ArrayList<String> getCurrentState() {
        ArrayList<String> response = new ArrayList<String> ();

        if (position.getX() == 0) {
            response.add("MOVE_SCREEN_LEFT");
        } else
        if (position.getX() == ApplicationGUI.Main.CurrentResolutionW - 1) {
            response.add("MOVE_SCREEN_RIGHT");
        }

        if (position.getY() == 0) {
            response.add("MOVE_SCREEN_UP");
        } else
        if (position.getY() == ApplicationGUI.Main.CurrentResolutionH - 1) {
            response.add("MOVE_SCREEN_DOWN");
        }

        return response;
    }
}