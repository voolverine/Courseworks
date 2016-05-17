package ApplicationGUI;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by volverine on 5/17/16.
 */
public class LevelsController {

    public Label level1;
    public Label level2;

    public void initialize() {

        level1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                level1.setTextFill(Color.DARKORANGE);
            }
        });

        level1.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                level1.setTextFill(Color.GREEN);
            }
        });

        level2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                level2.setTextFill(Color.DARKORANGE);
            }
        });

        level2.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                level2.setTextFill(Color.GREEN);
            }
        });

        int max_level_can = 1;

        try {
            File file = new File("save");
            if (!file.exists()) {
                file.createNewFile();
                max_level_can = 1;
            } else {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                max_level_can = Math.max(max_level_can, Integer.parseInt(line));
                reader.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        if (max_level_can < 2) {
            level2.setDisable(true);
        }
    }
}
