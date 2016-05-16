package World.Enemies;

import World.DrawableObject;
import World.Time;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by volverine on 5/16/16.
 */
public class Wave {
    private Time time;
    private boolean finished = false;
    private long wave_finish_time;
    private ArrayList<DrawableObject> mapObj;
    ArrayList<Pair<Enemy, Long>> wave;
    ArrayList<Boolean> was;

    public void updateWave() {
        if (finished) {
            return;
        }
        if (time.getCurrentGameTimeMillis() > wave_finish_time) {
            finished = false;
        }

        for (int i = 0; i < wave.size(); i++) {
            if (!was.get(i) && time.getCurrentGameTimeMillis() >= wave.get(i).getValue()) {
                mapObj.add(wave.get(i).getKey());
                was.set(i, true);
            }
        }
    }

    public Wave(ArrayList<DrawableObject> mapObj, Time time, ArrayList<Pair<Enemy, Long>> wave, long wave_finish_time) {
        this.time = time;
        this.mapObj = mapObj;
        this.wave = wave;
        was = new ArrayList<>();
        for (int i = 0; i < wave.size(); i++) {
            was.add(false);
        }
        this.wave_finish_time = wave_finish_time;
    }
}
