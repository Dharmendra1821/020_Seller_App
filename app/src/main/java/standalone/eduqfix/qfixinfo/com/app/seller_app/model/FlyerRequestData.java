package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import java.util.ArrayList;
import java.util.List;

public class FlyerRequestData {

    private String id;
    private String title;
    private String logo;
    private String page_type;
    private String view_mode;
    private String banner;
    private String summary;
    private String contact_details;
    private String store_address;
    private String store_information;
    private int is_modified;

    public int getIs_modified() {
        return is_modified;
    }

    public void setIs_modified(int is_modified) {
        this.is_modified = is_modified;
    }

    public int getSeller_id() {
        return Seller_id;
    }

    public void setSeller_id(int seller_id) {
        Seller_id = seller_id;
    }

    private int Seller_id;

    List<FlyerProduct> product_data;

    public void setProduct_data(List<FlyerProduct> product_data) {
        this.product_data = product_data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPage_type() {
        return page_type;
    }

    public void setPage_type(String page_type) {
        this.page_type = page_type;
    }

    public String getView_mode() {
        return view_mode;
    }

    public void setView_mode(String view_mode) {
        this.view_mode = view_mode;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContact_details() {
        return contact_details;
    }

    public void setContact_details(String contact_details) {
        this.contact_details = contact_details;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getStore_information() {
        return store_information;
    }

    public void setStore_information(String store_information) {
        this.store_information = store_information;
    }


}
