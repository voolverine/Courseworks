package World;

import World.Enemies.Enemy;
import World.Enemies.Wave;
import World.Towers.MainTower;

import java.util.ArrayList;

/**
 * Created by volverine on 5/17/16.
 */
public class FinishLevelChecker {
    MainTower mainTower;
    ArrayList<DrawableObject> mapObj;
    Time time;
    ArrayList<Long> waves;

    private boolean isMainDead() {
        return mainTower.getHealthPoints().isKilled();
    }

    private boolean wavesFinished() {
        for (int i = 0; i < waves.size(); i++) {
            long diff = time.getDifferenceMillis(waves.get(i));

            if (diff > 0) {
                return true;
            }
        }

        return false;
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

    public String updateGameStatus() {
        if (isMainDead()) {
            return "LOSE";
        }

        if (wavesFinished() && !isAnyAliveEnemy()) {
            return "WIN";
        }

        return "GAMING";
    }

    public FinishLevelChecker(MainTower mainTower, ArrayList<DrawableObject> mapObj, Time time, ArrayList<Wave> waves) {
        this.mainTower = mainTower;
        this.mapObj = mapObj;
        this.time = time;

        for (int i = 0; i < waves.size(); i++) {
            this.waves.add(waves.get(i).getWave_finish_time());
        }
    }
}
