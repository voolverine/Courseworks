package World;

import ApplicationGUI.ImageManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by volverine on 5/12/16.
 */
public class ProductGhost {
    private ImageView imageView;
    private Integer ImageID;
    private MouseHandler mouseHandler;

    private boolean need_to_draw = false;

    public void setNeed_to_draw(boolean value) {
        need_to_draw = value;
    }

    public ProductGhost(ImageView imageView, Integer ImageID, MouseHandler mouseHandler) {
        this.imageView = imageView;
        this.ImageID = ImageID;
        this.mouseHandler = mouseHandler;
        this.need_to_draw = true;
    }

    public void Draw() {
        if (need_to_draw) {
            imageView.setOpacity(1);
        } else {
            imageView.setOpacity(0);
        }

        Image product = ImageManager.getInstance().getImage(ImageID);
        imageView.setImage(product);

        int mouse_x = mouseHandler.getPosition().getX() - (int)product.getWidth() / 2;
        int mouse_y = mouseHandler.getPosition().getY() - (int)product.getHeight() / 2;
        imageView.setX(mouse_x);
        imageView.setY(mouse_y);
    }
}
