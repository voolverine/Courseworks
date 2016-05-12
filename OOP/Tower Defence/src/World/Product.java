package World;

/**
 * Created by volverine on 5/12/16.
 */
public class Product {
    private String class_name;
    private int price;

    public String getClass_name() {
        return class_name;
    }


    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public Product() {}
    public Product(String class_name, int price) {
        this.class_name = class_name;
        this.price = price;
    }
}
