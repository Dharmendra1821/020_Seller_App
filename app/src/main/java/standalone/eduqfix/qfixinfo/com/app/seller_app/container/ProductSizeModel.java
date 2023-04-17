package standalone.eduqfix.qfixinfo.com.app.seller_app.container;

public class ProductSizeModel {

    private int id;
    private String title;
    private int productid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    private String sku;
    private String label;
    private String price;
    private String attributeid;
    private String index;

    public String getAttributeid() {
        return attributeid;
    }

    public void setAttributeid(String attributeid) {
        this.attributeid = attributeid;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public ProductSizeModel(){

    }
    public ProductSizeModel(int id,String attributeid,int productid,String title, String label, String price,String sku,String index) {
        this.id = id;
        this.attributeid = attributeid;
        this.title = title;
        this.productid = productid;
        this.sku = sku;
        this.label = label;
        this.price = price;
        this.index  = index;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
