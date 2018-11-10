package application.model;
import static application.Supermarket.*;
import java.util.Random;

/**
 * Created by Long laptop on 9/24/2018.
 */
public class Product {
    private int productId;
    private String productName;
    private int checkoutProcessingTime;

    /**
     * Create a product with random checkout processing time between 50 to 500 mini second
     */
    public Product() {
        checkoutProcessingTime = RANDOM.nextInt(500) + 200;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCheckoutProcessingTime() {
        return checkoutProcessingTime;
    }

    public void setCheckoutProcessingTime(int checkoutProcessingTime) {
        this.checkoutProcessingTime = checkoutProcessingTime;
    }
}
