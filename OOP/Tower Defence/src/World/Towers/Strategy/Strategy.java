package World.Towers.Strategy;


import World.DrawableObject;
import World.Towers.Tower;

import java.util.ArrayList;

/**
 * Created by volverine on 5/12/16.
 */

public interface Strategy {
    public void Action(Tower tower, ArrayList<DrawableObject> mapObj);
}
