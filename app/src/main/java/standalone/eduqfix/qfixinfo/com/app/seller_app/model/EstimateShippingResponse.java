package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

public class EstimateShippingResponse {

    @SerializedName("carrier_code")
    private String carrier_code;
    @SerializedName("method_code")
    private String method_code;
    @SerializedName("carrier_title")
    private String carrier_title;

    public String getCarrier_code() {
        return carrier_code;
    }

    public void setCarrier_code(String carrier_code) {
        this.carrier_code = carrier_code;
    }

    public String getMethod_code() {
        return method_code;
    }

    public void setMethod_code(String method_code) {
        this.method_code = method_code;
    }

    public String getCarrier_title() {
        return carrier_title;
    }

    public void setCarrier_title(String carrier_title) {
        this.carrier_title = carrier_title;
    }

    public String getMethod_title() {
        return method_title;
    }

    public void setMethod_title(String method_title) {
        this.method_title = method_title;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @SerializedName("method_title")
    private String method_title;
    @SerializedName("amount")
    private float amount;



}
