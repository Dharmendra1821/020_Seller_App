package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darshan on 29/4/19.
 */

public class Request {

    @SerializedName("sellers")
    private List<Integer> sellers = null;

    public List<Integer> getSellers() {
        return sellers;
    }

    public void setSellers(List<Integer> sellers) {
        this.sellers = sellers;
    }











 @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("adminToken")
    @Expose
    private String adminToken;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getAdminToken() {
        return adminToken;
    }

    public void setAdminToken(String adminToken) {
        this.adminToken = adminToken;
    }



}
