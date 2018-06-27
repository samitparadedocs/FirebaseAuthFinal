package info.androidhive.firebase.model;

/**
 * Created by Samit
 */
public class ProductModel {

    int id;
    String productName;
    double price;
    String unit;
    boolean ischecked = true;
    double productCost;
    double productQuantity;

    public ProductModel(int id, String productName, double price, String unit) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.unit = unit;
    }

    public ProductModel(String productName, Double price, String unit) {
        this(0, productName, price, unit);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public double getProductCost() {
        return productCost;
    }

    public void setProductCost(double productCost) {
        this.productCost = productCost;
    }

    public double getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(double productQuantity) {
        this.productQuantity = productQuantity;
    }
}
