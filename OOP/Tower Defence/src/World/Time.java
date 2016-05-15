package World;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Arc;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by volverine on 5/14/16.
 */
public class Time extends DrawableObject implements IMovable {
    private Label label;
    private long current_gameTimeMillis = 0;
    private long previous_timeMillis = 0;

    public Time(Label label) {
        super(new Position(-50, -50));
        this.label = label;
    }


    private String MillisToString(long time) {
        String result = "";

        time /= 1000;
        long seconds = time % 60;
        time /= 60;
        long minutes = time % 60;

        if (minutes < 10) {
            result += '0';
        }
        result += String.format("%d:", minutes);
        if (seconds < 10) {
            result += '0';
        }
        result += String.format("%d", seconds);

        return result;
    }

    private void setCurrentTime() {
        label.setText(String.format("Current time = " + MillisToString(current_gameTimeMillis)));
    }

    private void updateCurrentTime() {
        long current_timeMillis = Calendar.getInstance().getTimeInMillis();
        if (current_timeMillis - previous_timeMillis > 200) {
            previous_timeMillis = current_timeMillis;
            return;
        }

        current_gameTimeMillis += current_timeMillis - previous_timeMillis;
        previous_timeMillis = current_timeMillis;
    }

    public long getCurrentGameTimeSeconds() {
        return current_gameTimeMillis / 1000;
    }

    public long getDifferenceSeconds(long time_in_seconds) {
        return current_gameTimeMillis / 1000 - time_in_seconds;
    }

    public boolean timeGoneAfter(long time_in_seconds, long time_need_to_go) {
        if (getDifferenceSeconds(time_in_seconds) >= time_need_to_go) {
            return true;
        }

        return false;
    }

    public void Action(ArrayList<DrawableObject> mapObj) {
        updateCurrentTime();
        setCurrentTime();
    }
}
