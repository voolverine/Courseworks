package World.Towers;

/**
 * Created by volverine on 4/19/16.
 */
public class TowersPanel {
    private static TowersPanel ourInstance = new TowersPanel();

    public static TowersPanel getInstance() {
        return ourInstance;
    }

    private TowersPanel() {
    }
}
