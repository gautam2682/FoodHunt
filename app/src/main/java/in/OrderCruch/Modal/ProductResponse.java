package in.OrderCruch.Modal;

/**
 * Created by gautam on 14/12/16.
 */

public class ProductResponse  {
    private ProductVersion[] products;

    private User[] user;
    public User[] getUsers(){return user;}

    public ProductVersion[] getProducts() {
        return products;
    }
}
