package info.androidhive.firebase.fragments;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DealDetailModel implements Serializable {

    String name;
    byte age;
    private int integerUnigueId;
    private String productTitle;
    private String description;
    private String price;
    private String address;
    private String discount;
    private String serverImagePath;
    private String products;
    private String contact_no;
    private String merchant_id;

    public int getIntegerUnigueId() {
        return integerUnigueId;
    }

    public void setIntegerUnigueId(int integerUnigueId) {
        this.integerUnigueId = integerUnigueId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getServerImagePath() {
        return serverImagePath;
    }

    public void setServerImagePath(String serverImagePath) {
        this.serverImagePath = serverImagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getmDiscount() {
        return discount;
    }

    public void setmDiscount(String mDiscount) {
        this.discount = mDiscount;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("integerUnigueId",""+ integerUnigueId);
        result.put("productTitle",""+ productTitle);
        result.put("description",""+ description);
        result.put("address",address);

        result.put("serverImagePath", serverImagePath);
        result.put("products", products);
        result.put("contact_no", contact_no);
        result.put("merchant_id", merchant_id);

        return result;
    }
}