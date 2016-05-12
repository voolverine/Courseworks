package World;

import java.util.ArrayList;

/**
 * Created by volverine on 5/12/16.
 */
public class Shop {
    private ArrayList<Product> all_products;


    public void addProduct(Product new_product) {
        all_products.add(new_product);
    }

    public ArrayList<Product> getProducts() {
        return all_products;
    }


    public Shop() {
        all_products = new ArrayList<Product> ();
    }
}
