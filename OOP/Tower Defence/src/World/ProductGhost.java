package World;

import ApplicationGUI.ImageManager;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

/**
 * Created by volverine on 5/12/16.
 */
public class ProductGhost extends DrawableObject {
    private ImageView imageView;
    public Integer ImageID;
    private MouseHandler mouseHandler;

    private boolean need_to_draw = false;
    private boolean can_be_build = false;

    public boolean canBuild() {
        return can_be_build;
    }

    public void setNeed_to_draw(boolean value) {
        need_to_draw = value;
    }

    public ProductGhost(Position position, ImageView imageView, Integer ImageID, MouseHandler mouseHandler) {
        super(position);
        this.ImageID = ImageID;
        this.imageView = imageView;
        this.mouseHandler = mouseHandler;
        this.need_to_draw = true;
    }


    void updateCanBeBuild(ArrayList<DrawableObject> mapObj) {

        for (DrawableObject obj: mapObj) {
             if (obj instanceof TowerBarier) {
                if (!((TowerBarier) obj).canBuild(position, ImageID)) {
                    can_be_build = false;
                    need_to_draw = false;
                    return;
                }
            }
        }

        for (DrawableObject obj: mapObj) {
            if (!(obj instanceof TowerBarier) && DrawableObject.isIntersects(obj, this)) {
                can_be_build = false;
                need_to_draw = true;
                return;
            }
        }

        need_to_draw = true;
        can_be_build = true;
    }

    void updatePosition() {
        position.setX(mouseHandler.getPosition().getX());
        position.setY(mouseHandler.getPosition().getY());
    }


    void Disable() {
        imageView.setX(-500);
        imageView.setY(-500);
    }

    public void Draw(GraphicsContext gc) {
        if (need_to_draw) {
            imageView.setOpacity(1);
        } else {
            imageView.setOpacity(0);
            return;
        }

        if (can_be_build) {
            imageView.setBlendMode(BlendMode.GREEN);
        } else {
            imageView.setBlendMode(BlendMode.RED);
        }

        Image product = ImageManager.getInstance().getImage(ImageID);
        imageView.setImage(product);
        updatePosition();

        int mouse_x = position.getX() - (int)product.getWidth() / 2;
        int mouse_y = position.getY() - (int)product.getHeight() / 2;

        imageView.setX(mouse_x);
        imageView.setY(mouse_y);
    }
}
