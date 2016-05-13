package World;

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Created by volverine on 5/13/16.
 */
public class Trash {
    private ImageView imageView;
    private boolean active = false;

    public Trash() {}
    public Trash(ImageView imageView) {
        this.imageView = imageView;


        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });
    }


}
