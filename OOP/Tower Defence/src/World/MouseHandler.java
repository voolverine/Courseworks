package World;

import ApplicationGUI.Main;
import javafx.application.Application;
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
    private MouseEvent action = null;
    private boolean returned = false;
    private Position position = new Position(1, 1);


    public Position getPosition() {
        return position;
    }


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


    public MouseEvent getState() {
        if (action != null) {
            if (returned) {
                action = null;
            } else {
                returned = true;
                return action;
            }
        }

        return null;
    }
}