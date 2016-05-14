package World;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Arc;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

/**
 * Created by volverine on 5/14/16.
 */
public class Time extends DrawableObject implements IMovable {
    private Label label;
    private Date currentTime;

    public Time(Label label) {
        super(new Position(-50, -50));
        this.label = label;
        currentTime = new Date(0);
    }

    private void setCurrentTime() {
        label.setText(String.format("Current time = "));
    }

    private void updateCurrentTime() {

    }

    public void Action(ArrayList<DrawableObject> mapObj) {
        updateCurrentTime();
        setCurrentTime();
    }
}
