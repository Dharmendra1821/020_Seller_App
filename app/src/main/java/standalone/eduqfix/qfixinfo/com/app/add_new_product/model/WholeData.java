package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

public class WholeData {

    public String type;
    public int set;
    public String seller_id;
    public String id;
    public String product_id;
    public int approve_status;
    public int old_status;

    public int getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(int approve_status) {
        this.approve_status = approve_status;
    }

    public int getOld_status() {
        return old_status;
    }

    public void setOld_status(int old_status) {
        this.old_status = old_status;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    Product product;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
