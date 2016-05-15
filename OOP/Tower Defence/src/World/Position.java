package World;

import ApplicationGUI.Main;
import javafx.geometry.Pos;
import javafx.scene.paint.Material;

/**
 * Created by volverine on 4/19/16.
 */
public class Position {
    private int x;
    private int y;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    public Position() {
        this.x = 0;
        this.y = 0;
    }


    public int getX() {
        return x;
    }
    public boolean setX(int x) {
        this.x = x;
        return true;
    }


    public int getY() {
        return y;
    }
    public boolean setY(int y) {
        this.y = y;
        return true;
    }


    public static double dist(Position p1, Position p2) {
        double d1 = (p1.getX() - p2.getX()) * (p1.getX() - p2.getX());
        double d2 = (p1.getY() - p2.getY()) * (p1.getY() - p2.getY());
        return Math.sqrt(d1 + d2);
    }


    public void moveUp(int dy) {
        this.y -= Math.abs(dy);
    }

    public void moveDown(int dy) {
        this.y += Math.abs(dy);
    }

    public void moveRight(int dx) {
        this.x += Math.abs(dx);
    }

    public void moveLeft(int dx) {
        this.x -= Math.abs(dx);
    }

    public boolean equals(Position other) {
        return this.x == other.getX() && this.y == other.getY();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Position) {
            return equals((Position)obj);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return x * 3571 + y;
    }

    public static double dist(DrawableObject do1, DrawableObject do2) {
        return dist(do1.getPosition(), do2.getPosition());
    }
}
