package standalone.eduqfix.qfixinfo.com.app.seller_app.model;


import java.util.List;

public class BundleContainerModel {

    public int mainoptionId;
    public String sku;
    public String title;
    public String type;
    public List<ProductLinkModel> productLinkModelList;

    public  BundleContainerModel(){

    }


    public List<ProductLinkModel> getProductLinkModelList() {
        return productLinkModelList;
    }

    public void setProductLinkModelList(List<ProductLinkModel> productLinkModelList) {
        this.productLinkModelList = productLinkModelList;
    }

    public int getMainoptionId() {
        return mainoptionId;
    }

    public void setMainoptionId(int mainoptionId) {
        this.mainoptionId = mainoptionId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
