package World.Enemies.Strategy;

import World.DrawableObject;
import World.Enemies.Enemy;
import World.Position;
import World.Time;
import javafx.geometry.Pos;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by volverine on 5/15/16.
 */
public class ForwardMainTower implements Strategy {

    private static int dx[] = {-3, -2, -2, -2, -2, -2, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3};
    private static int dy[] = {0, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -3, -2, -1, 0, 1, 2, 3, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, 0};

    private ArrayList<Position> way;
    private Enemy enemy;
    private int wait_time = 25;
    private int waiting = 0;
    private boolean terminated = false;
    long previous_step_time;
    Time time;

    public ForwardMainTower(Enemy enemy, Time time) {
        this.enemy = enemy;
        way = new ArrayList<Position>();
        previous_step_time = time.getCurrentGameTimeMillis();
        this.time = time;
    }


    private void getWay(Map<Position, Pair<Integer, Position>> hashed, Position last_position) {
        Pair<Integer, Position> dist = hashed.get(last_position);
        way.clear();

        while (dist.getKey() != 0) {
            way.add(last_position);
            dist = hashed.get(last_position);
            last_position = dist.getValue();
        }


        for (int i = 0; i < (int)way.size() / 2; i++) {
            Position temp = way.get(i);
            way.set(i, way.get((int)way.size() - 1 - i));
            way.set((int)way.size() - 1 - i, temp);
        }

        /*
        for (int i = 0; i < way.size(); i++) {
            System.out.println(String.format("%d %d", way.get(i).getX(), way.get(i).getY()));
        }
        */
    }


    private boolean neighBoors(Position position1, Position position2) {
        //System.out.println(Position.dist(position1, position2));
        if (Position.dist(position1, position2) <= 50.0) {
            return true;
        }

        return false;
    }


    private boolean isValid(Position pos) {
        if (pos.getY() < -300 || 1500 < pos.getY() ||
                pos.getX() < -300 || 1500 < pos.getX()) {
            return false;
        }

        return true;
    }

    private void create_way(ArrayList<DrawableObject> mapObj) {
        Queue<Position> q = new LinkedList<Position>();
        Map<Position, Pair<Integer, Position>> hashed = new HashMap<Position, Pair<Integer, Position>>();
        q.add(new Position(enemy.getPosition()));
        hashed.put(new Position(enemy.getPosition()), new Pair<Integer, Position>(0, new Position(-1, -1)));
        Position last_neighboor = null;
        terminated = false;


        while (!q.isEmpty()) {
            Position cur = q.peek();
            Position previous = hashed.get(cur).getValue();

            if (neighBoors(cur, enemy.getMainTower().getPosition())) {
                break;
            }

            if (hashed.get(cur).getKey() >= 600) {
                terminated = true;
                break;
            }

            q.poll();

            int cur_x = cur.getX();
            int cur_y = cur.getY();

            double dist_toMainTower = 999999.0;
            int best_way = -1;

            for (int i = 0; i < dx.length; i++) {
                int new_x = cur_x + dx[i];
                int new_y = cur_y + dy[i];
                Position new_position = new Position(new_x, new_y);
                boolean free = DrawableObject.isFree(new_position, enemy, mapObj);
                last_neighboor = cur;

                if (!hashed.containsKey(new_position) && isValid(new_position)
                        && free) {
                    double new_dist = Position.dist(new_position, enemy.getMainTower().getPosition());
                    if (new_dist < dist_toMainTower && Position.dist(cur, previous) + 1.0 < Position.dist(new_position, previous)) {
                        dist_toMainTower = new_dist;
                        best_way = i;
                    }
                }
            }

            if (best_way != -1) {
                Position new_position = new Position(cur_x + dx[best_way], cur_y + dy[best_way]);
                q.add(new_position);
                hashed.put(new_position, new Pair<Integer, Position>(hashed.get(cur).getKey() + 1, cur));
//                System.out.println(String.format("%d %d", new_position.getX(), new_position.getY()));
            }
        }

        if (terminated || q.isEmpty()) {
            getWay(hashed, last_neighboor);
        } else
        {
            Position final_step = q.peek();
            getWay(hashed, final_step);
        }
    }


    private int find_me() {
        for (int i = 0; i < (int)way.size(); i++) {
            if (enemy.getPosition().equals(way.get(i))) {
                /*
                System.out.println(String.format("curx = %d cury = %d cmpx = %d cmpty = %d",
                        enemy.getPosition().getX(), enemy.getPosition().getY(),
                        way.get(i).getX(), way.get(i).getY()));
                System.out.println(i);
                */
                return i;
            }
        }

        return -1;
    }


    public void Action(ArrayList<DrawableObject> mapObj) {
        if (!time.timeGoneAfter(previous_step_time, enemy.getMovingSpeed())) {
            return;
        }
        previous_step_time = time.getCurrentGameTimeMillis();

        int ind = find_me();

        if (ind == -1) {
            create_way(mapObj);
            ind = find_me();
        }


        if (ind != -1) {
            if (ind == (int)way.size() - 1) {
                return;
            }

            if (!DrawableObject.isFree(way.get(ind + 1), enemy, mapObj)) {
                if (waiting >= wait_time) {
                    System.out.println("recreating");
                    create_way(mapObj);
                    ind = find_me();
                } else {
                    waiting++;
                    return;
                }
            }

            waiting = 0;
            ind++;
            if (ind == way.size()) {
                return;
            }
            /*
            System.out.println(String.format("was x = %d y = %d \n new x = %d y = %d", enemy.getPosition().getX(),
                    enemy.getPosition().getY(), way.get(ind).getX(), way.get(ind).getY()));
            */
            if (DrawableObject.isFree(way.get(ind), enemy, mapObj)) {
                enemy.setPosition(way.get(ind).getX(), way.get(ind).getY());
            }
        }
    }
}
