package World;


import ApplicationGUI.ImageManager;
import ApplicationGUI.Main;
import World.Enemies.Wave;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by volverine on 5/16/16.
 */
public class ProgressBar {
    public static Integer ImageID = new Integer(10001);
    private Time time;
    private ArrayList<Long> waves;
    private long last_wave_time;


    private int width = 200;
    private int height = 10;
    private int margin = 1;
    private int corner1_x = Main.CurrentResolutionW - width - 50;
    private int corner1_y = height + 10;


    public boolean isWaveComming() {
        long cur_time = time.getCurrentGameTimeMillis();

        for (int i = 0; i < waves.size(); i++) {
            long diff = waves.get(i);

            if (100 <= diff && diff <= 5000) {
                return true;
            }
        }

        return false;
    }


    private double getPercentTime(long cur_time) {
        return Math.min(1.0, cur_time / (double)last_wave_time);
    }


    public void Draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(corner1_x, corner1_y, width, height);

        int inner1_x = corner1_x + margin;
        int inner1_y = corner1_y + margin;

        int inner_width = width - 2 * margin;
        int inner_height = height - 2 * margin;
        int green_width = (int)((double) inner_width * getPercentTime(time.getCurrentGameTimeMillis()));
        int red_width = inner_width - green_width;

        gc.setFill(Color.GREEN);
        gc.fillRect(inner1_x, inner1_y, green_width, inner_height);
        gc.setFill(Color.RED);
        gc.fillRect(inner1_x + green_width, inner1_y, red_width, inner_height);
        Image img = ImageManager.getInstance().getImage(ImageID);

        for (int i = 0; i < waves.size(); i++) {
            long wave_time = waves.get(i);

            double percent_time = getPercentTime(wave_time);
            int inner_x = inner1_x + (int)((double) inner_width * percent_time);
            gc.drawImage(img, inner_x - (int)img.getWidth(), inner1_y - (int)img.getHeight());
        }
    }


    public ProgressBar(Time time, ArrayList<Wave> waves) {
        this.time = time;
        this.waves = new ArrayList<>();
        for (int i = 0; i < waves.size(); i++) {
            this.waves.add(waves.get(i).getWave_finish_time());
        }
        last_wave_time = this.waves.get(waves.size() - 1);
    }
}
