package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.OrderRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;

public class MainRequest {
    OrderRequest request;

    public OrderRequest getRequest() {
        return request;
    }

    public void setRequest(OrderRequest request) {
        this.request = request;
    }

}
