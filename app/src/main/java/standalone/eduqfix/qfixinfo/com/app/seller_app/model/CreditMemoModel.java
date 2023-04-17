package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class CreditMemoModel {
    private String name;
    private String sku;
    private String qty;
    private String entity_id;
    private String shipping_amount;
    private String base_subtotal;
    private String order_item_id;

    public String getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(String order_item_id) {
        this.order_item_id = order_item_id;
    }

    public String getBase_price() {
        return base_price;
    }

    public void setBase_price(String base_price) {
        this.base_price = base_price;
    }

    private String grand_total;
    private String base_price;

    public CreditMemoModel(String name, String sku, String qty, String entity_id, String shipping_amount, String base_subtotal, String grand_total, String base_price,String order_item_id) {
        this.name = name;
        this.sku = sku;
        this.qty = qty;
        this.entity_id = entity_id;
        this.shipping_amount = shipping_amount;
        this.base_subtotal = base_subtotal;
        this.grand_total = grand_total;
        this.base_price = base_price;
        this.order_item_id = order_item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    public String getShipping_amount() {
        return shipping_amount;
    }

    public void setShipping_amount(String shipping_amount) {
        this.shipping_amount = shipping_amount;
    }

    public String getBase_subtotal() {
        return base_subtotal;
    }

    public void setBase_subtotal(String base_subtotal) {
        this.base_subtotal = base_subtotal;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }
}
