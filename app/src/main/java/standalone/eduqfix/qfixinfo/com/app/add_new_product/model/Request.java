package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;

public class Request {

    public String seller_id;
    WholeData wholedata;

    public ConfigDetails getData() {
        return data;
    }

    public void setData(ConfigDetails data) {
        this.data = data;
    }

    ConfigDetails data;

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public WholeData getWholedata() {
        return wholedata;
    }

    public void setWholedata(WholeData wholedata) {
        this.wholedata = wholedata;
    }
}
