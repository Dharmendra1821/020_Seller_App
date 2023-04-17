package standalone.eduqfix.qfixinfo.com.app.seller_app.community.model;

public class CommunitySellerListModel {

    private String sellerName;
    private String sellerContactNo;
    private String shopName;
    private String sellerEmail;
    private String status;

    public CommunitySellerListModel(String sellerName, String sellerContactNo, String shopName, String sellerEmail, String status, String id, String sellerId) {
        this.sellerName = sellerName;
        this.sellerContactNo = sellerContactNo;
        this.shopName = shopName;
        this.sellerEmail = sellerEmail;
        this.status = status;
        this.id = id;
        this.sellerId = sellerId;
    }

    private String id;
    private String sellerId;

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerContactNo() {
        return sellerContactNo;
    }

    public void setSellerContactNo(String sellerContactNo) {
        this.sellerContactNo = sellerContactNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
