package World.Enemies.Strategy;

import World.DrawableObject;

import java.util.ArrayList;

/**
 * Created by volverine on 5/12/16.
 */
public interface Strategy {
    void Action(ArrayList<DrawableObject> mapObj);
}
