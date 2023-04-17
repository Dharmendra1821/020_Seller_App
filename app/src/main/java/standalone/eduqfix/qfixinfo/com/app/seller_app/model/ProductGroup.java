package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductGroup {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("price")
    @Expose
    private Integer price;


    @SerializedName("type_id")
    @Expose
    private String typeId;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
