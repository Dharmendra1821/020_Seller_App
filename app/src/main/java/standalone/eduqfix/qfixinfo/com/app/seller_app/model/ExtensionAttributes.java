package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darshan on 29/4/19.
 */

public class ExtensionAttributes {

   // @SerializedName("tax_grandtotal_details")
    //private List<Object> taxGrandtotalDetails = null;


    @SerializedName("website_ids")
    @Expose
    private List<Integer> websiteIds = null;
    @SerializedName("category_links")
    @Expose
    private List<CategoryLink> categoryLinks = null;
    @SerializedName("stock_item")
    @Expose
    private StockItem stockItem;


    @SerializedName("bundle_product_options")
    @Expose
    private List<BundleProductOption> bundleProductOptions = null;

    public List<BundleProductOption> getBundleProductOptions() {
        return bundleProductOptions;
    }

    public void setBundleProductOptions(List<BundleProductOption> bundleProductOptions) {
        this.bundleProductOptions = bundleProductOptions;
    }






    public List<Integer> getWebsiteIds() {
        return websiteIds;
    }

    public void setWebsiteIds(List<Integer> websiteIds) {
        this.websiteIds = websiteIds;
    }

    public List<CategoryLink> getCategoryLinks() {
        return categoryLinks;
    }

    public void setCategoryLinks(List<CategoryLink> categoryLinks) {
        this.categoryLinks = categoryLinks;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }




}

