package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

public class AddProductListingModel {

       private String productname;
       private String sku;
       private String imageurl;
       private String price;
       private String productId;
       private String file;


    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    private String valueId;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    private String entityId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public AddProductListingModel(){

    }

    public AddProductListingModel(String productname, String sku, String imageurl, String price, String productId, String file, String valueId, String entityId) {
        this.productname = productname;
        this.sku = sku;
        this.imageurl = imageurl;
        this.price = price;
        this.productId = productId;
        this.file = file;
        this.valueId = valueId;
        this.entityId = entityId;
    }


    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String  getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
