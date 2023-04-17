package standalone.eduqfix.qfixinfo.com.app.seller_app.container;

public class ConfigProductModel {

    public int id;
    public int productid;
    public String productimage;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public String productname;
    public String productprice;
    public String productsmallimage;
    public String producttype;

    public ConfigProductModel(int id,int productid, String productimage, String productname, String productprice, String productsmallimage, String producttype, String productsku, String productthumbnail) {
        this.id = id;
        this.productid = productid;
        this.productimage = productimage;
        this.productname = productname;
        this.productprice = productprice;
        this.productsmallimage = productsmallimage;
        this.producttype = producttype;
        this.productsku = productsku;
        this.productthumbnail = productthumbnail;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getProductsmallimage() {
        return productsmallimage;
    }

    public void setProductsmallimage(String productsmallimage) {
        this.productsmallimage = productsmallimage;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public String getProductsku() {
        return productsku;
    }

    public void setProductsku(String productsku) {
        this.productsku = productsku;
    }

    public String getProductthumbnail() {
        return productthumbnail;
    }

    public void setProductthumbnail(String productthumbnail) {
        this.productthumbnail = productthumbnail;
    }

    public String productsku;
    public String productthumbnail;

}
