package World;

import ApplicationGUI.Main;

/**
 * Created by volverine on 5/11/16.
 */
public class HealthPoints {
    private int current_hp;
    private int maximal_hp;
    private boolean killed = false;
    private long last_hurtTime;

    public HealthPoints(int maxHealth) {
        current_hp = maxHealth;
        maximal_hp = maxHealth;

        last_hurtTime = 0;
    }

    public HealthPoints(int curHealth, int maxHealth) {
        current_hp = curHealth;
        maximal_hp = maxHealth;
    }


    public boolean isKilled() {
        if (current_hp <= 0) {
            killed = true;
        }

        return killed;
    }


    private void update_last_hurtTime() {
        last_hurtTime = World.time.getCurrentGameTimeMillis();
    }

    public long getLast_hurtTime() {
        return last_hurtTime;
    }


    public void hurt(int damage) {
        current_hp = Math.max(current_hp - damage, 0);
        update_last_hurtTime();
        isKilled();

        return;
    }


    public void updateMaxHP(int delta) {
        maximal_hp += delta;
        return;
    }

    public void healAbs() {
        current_hp = maximal_hp;
        return;
    }

    public void heal(int delta) {
        current_hp = Math.min(current_hp + delta, maximal_hp);
        return;
    }


    public int get_percentageHP() {
        double current = current_hp * 1.0 / maximal_hp * 100.0;

        return (int)current;
    }

}
