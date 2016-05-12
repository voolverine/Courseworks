package World;

import ApplicationGUI.ImageManager;
import javafx.event.EventHandler;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;


/**
 * Created by volverine on 5/12/16.
 */
public class Product {
    private String class_name;
    private int price;
    private ImageView imageView;
    private Integer ImageID;
    private boolean state;

    public String getClass_name() {
        return class_name;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public boolean getState() {
        if (state) {
            state = false;
            return true;
        }
        return false;
    }

    public Integer getImageID() {
        return ImageID;
    }

    public Product() {}
    public Product(String class_name, int price, Integer ImageID) {
        this.class_name = class_name;
        this.price = price;
        this.ImageID = ImageID;
        this.imageView = new ImageView(ImageManager.getInstance().getImage(ImageID));
        this.state = false;

        this.imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                state = true;
                //System.out.println(String.format("%s clicked", class_name));
            }
        });
    }

}
