package World.Enemies;

import World.*;
import World.Towers.MainTower;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import sun.awt.image.ImageWatched;

import javax.crypto.spec.PSource;
import java.util.*;

/**
 * Created by volverine on 5/11/16.
 */
public class Enemy extends DrawableObject implements IMovable, IHealthable {
    protected HealthPoints healthPoints;
    protected MainTower mainTower;
    ArrayList<Position> way;

    public HealthPoints getHealthPoints() {
        return healthPoints;
    }

    public Enemy(Position position, HealthPoints healthPoints, MainTower mainTower) {
        super(position);
        this.healthPoints = healthPoints;
        this.mainTower = mainTower;
        way = new ArrayList<Position>();
    }

/* TODO: not finished yet */
    private void create_way() {
        Queue<Position> q = new LinkedList<Position>();
        Map<Position, Integer> history= new HashMap<Position, Integer>();
        q.add(position);

        while (!q.isEmpty()) {
            Position cur = q.peek();
            q.poll();


        }
    }



    public void step(ArrayList<DrawableObject> mapObj) {
        if (way.size() == 0) {
            create_way();
        }


    }


    public void Draw(GraphicsContext gc) {}
    public void Action(ArrayList<DrawableObject> mapObj) {}
}
