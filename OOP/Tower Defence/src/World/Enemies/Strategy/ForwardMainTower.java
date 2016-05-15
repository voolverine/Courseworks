package World.Enemies.Strategy;

import World.DrawableObject;
import World.Enemies.Enemy;
import World.Position;
import javafx.geometry.Pos;
import javafx.util.Pair;

import javax.net.ssl.SSLContext;
import java.util.*;

/**
 * Created by volverine on 5/15/16.
 */
public class ForwardMainTower implements Strategy {

    private static int dx[] = {-3, -2, -2, -2, -2, -2, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3};
    private static int dy[] = {0, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -3, -2, -1, 0, 1, 2, 3, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, 0};

    private ArrayList<Position> way;
    private Enemy enemy;

    public ForwardMainTower(Enemy enemy) {
        this.enemy = enemy;
        way = new ArrayList<Position>();
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


        for (int i = 0; i < way.size(); i++) {
            System.out.println(String.format("%d %d", way.get(i).getX(), way.get(i).getY()));
        }
    }


    private boolean neighBoors(Position position1, Position position2) {
        System.out.println(Position.dist(position1, position2));
        if (Position.dist(position1, position2) <= 50.0) {
            return true;
        }

        return false;
    }


    private void create_way(ArrayList<DrawableObject> mapObj) {
        Queue<Position> q = new LinkedList<Position>();
        Map<Position, Pair<Integer, Position>> hashed = new HashMap<Position, Pair<Integer, Position>>();
        q.add(new Position(enemy.getPosition()));
        hashed.put(new Position(enemy.getPosition()), new Pair<Integer, Position>(0, new Position(-1, -1)));


        while (!q.isEmpty()) {
            Position cur = q.peek();

            if (neighBoors(cur, enemy.getMainTower().getPosition())) {
                break;
            }
            if (hashed.get(cur).getKey() >= 1500) {
                break;
            }

            q.poll();

            int cur_x = cur.getX();
            int cur_y = cur.getY();

            double dist = 999999.0;
            int best_way = -1;

            for (int i = 0; i < dx.length; i++) {
                int new_x = cur_x + dx[i];
                int new_y = cur_y + dy[i];
                Position new_position = new Position(new_x, new_y);

                if (!hashed.containsKey(new_position) && DrawableObject.isFree(new_position, enemy, mapObj)) {
                    double new_dist = Position.dist(new_position, enemy.getMainTower().getPosition());
                    if (new_dist < dist) {
                        dist = new_dist;
                        best_way = i;
                    }
                }
            }

            if (best_way != -1) {
                Position new_position = new Position(cur_x + dx[best_way], cur_y + dy[best_way]);
                q.add(new_position);
                hashed.put(new_position, new Pair<Integer, Position>(hashed.get(cur).getKey() + 1, cur));
            }
        }

        if (!q.isEmpty()) {
            Position final_step = q.peek();
            getWay(hashed, final_step);
        }
    }


    private int find_me() {
        for (int i = 0; i < (int)way.size(); i++) {
            if (enemy.getPosition().equals(way.get(i))) {
                System.out.println(String.format("curx = %d cury = %d cmpx = %d cmpty = %d",
                        enemy.getPosition().getX(), enemy.getPosition().getY(),
                        way.get(i).getX(), way.get(i).getY()));
                System.out.println(i);
                return i;
            }
        }

        return -1;
    }


    public void Action(ArrayList<DrawableObject> mapObj) {
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
                create_way(mapObj);
                ind = find_me();
            }

            if (ind == (int)way.size() - 1) {
                return;
            }


            ind++;
            System.out.println(String.format("was x = %d y = %d \n new x = %d y = %d", enemy.getPosition().getX(),
                    enemy.getPosition().getY(), way.get(ind).getX(), way.get(ind).getY()));
            enemy.setPosition(way.get(ind).getX(), way.get(ind).getY());
        }
    }
}
