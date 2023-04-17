package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class ConfigProductCartResponse {
    private Integer item_id;
    private String sku;
    private Integer qty;
    private String name;
    private Double price;

    public ProductOption getProductOption() {
        return productOption;
    }

    public void setProductOption(ProductOption productOption) {
        this.productOption = productOption;
    }

    private String product_type;
    private String quote_id;
    ProductOption productOption;



    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }


}
