package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class CustomerProductCartRequest {
    String quote_id;
    String qty_change;
    String sku;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }

    public String getQty_change() {
        return qty_change;
    }

    public void setQty_change(String qty_change) {
        this.qty_change = qty_change;
    }
}
