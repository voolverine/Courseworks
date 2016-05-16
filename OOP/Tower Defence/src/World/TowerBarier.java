package World;


/**
 * Created by volverine on 5/16/16.
 */
public class TowerBarier extends DrawableObject {
    private int corner1_x, corner1_y;
    private int corner2_x, corner2_y;

    public TowerBarier(int corner1_x, int corner1_y, int corner2_x, int corner2_y) {
        super(new Position(-1, -1));
        this.corner1_x = corner1_x;
        this.corner1_y = corner1_y;
        this.corner2_x = corner2_x;
        this.corner2_y = corner2_y;
    }

    public boolean canBuild(Position position) {
        if (corner1_x <= position.getX() && position.getX() <= corner2_x &&
                corner1_y <= position.getY() && position.getY() <= corner2_y) {
            return false;
        }

        return true;
    }
}
