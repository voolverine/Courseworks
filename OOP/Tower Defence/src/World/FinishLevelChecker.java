package World;

import World.Enemies.Enemy;
import World.Enemies.Wave;
import World.Towers.MainTower;
import javafx.scene.control.Label;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by volverine on 5/17/16.
 */
public class FinishLevelChecker {
    private String file_name = "save";
    MainTower mainTower;
    ArrayList<DrawableObject> mapObj;
    Time time;
    ArrayList<Long> waves;
    Label label;
    boolean writed;

    private boolean game_finished = false;

    private boolean isMainDead() {
        return mainTower.getHealthPoints().isKilled();
    }

    private boolean wavesFinished() {
        for (int i = 0; i < waves.size(); i++) {
            long diff = time.getDifferenceMillis(waves.get(i));

            if (diff < 0) {
                return false;
            }
        }

        return true;
    }

    private boolean isAnyAliveEnemy() {
        for (int i = 0; i < mapObj.size(); i++) {
            DrawableObject obj = mapObj.get(i);

            if (obj instanceof Enemy && !((Enemy) obj).getHealthPoints().isKilled()) {
                return true;
            }
        }

        return false;
    }


    public void write_to_file() {
        try {
            File file = new File("save");

            int current_max = mainTower.getLevel() + 1;
            if (!file.exists()) {
                file.createNewFile();
            } else {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                current_max = Math.max(current_max, Integer.parseInt(line));
                reader.close();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(String.format("%d", current_max));

            writer.close();
            writed = true;
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public String updateGameStatus() {
        if (isMainDead()) {
            label.setOpacity(1);
            label.setDisable(false);
            label.setText("You are lose ;(");
            return "LOSE";
        }

        if (wavesFinished() && !isAnyAliveEnemy()) {
            label.setOpacity(1);
            label.setDisable(false);
            label.setText("You are win !!!");
            if (!writed) {
                write_to_file();
            }
            return "WIN";
        }

        return "GAMING";
    }

    public FinishLevelChecker(MainTower mainTower, ArrayList<DrawableObject> mapObj, Time time, ArrayList<Wave> waves,
                                Label level_status) {
        this.mainTower = mainTower;
        this.mapObj = mapObj;
        this.time = time;
        this.waves = new ArrayList<>();

        for (int i = 0; i < waves.size(); i++) {
            this.waves.add(waves.get(i).getWave_finish_time());
        }
        this.label = level_status;
        this.label.setDisable(true);
        this.label.setOpacity(0);
        writed = false;
    }
}
