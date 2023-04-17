
package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductSKU {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("attribute_set_id")
    @Expose
    private Integer attributeSetId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("visibility")
    @Expose
    private Integer visibility;
    @SerializedName("type_id")
    @Expose
    private String typeId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;

    public ExtensionAttributes getExtensionAttributes() {
        return extensionAttributes;
    }

    public void setExtensionAttributes(ExtensionAttributes extensionAttributes) {
        this.extensionAttributes = extensionAttributes;
    }

    @SerializedName("product_links")
    @Expose
    private List<ProductLink> productLinks = null;
    @SerializedName("options")
    @Expose
    private List<Object> options = null;
    @SerializedName("media_gallery_entries")
    @Expose
    private List<MediaGalleryEntry> mediaGalleryEntries = null;
    @SerializedName("tier_prices")
    @Expose
    private List<Object> tierPrices = null;


    @SerializedName("min_price")
    @Expose
    private String minPrice;
    @SerializedName("max_price")
    @Expose
    private String maxPrice;

    @SerializedName("isProductEndorsed")
    @Expose
    private Boolean isProductEndorsed;
    @SerializedName("sellerid")
    @Expose
    private String sellerid;
    @SerializedName("sellername")
    @Expose
    private String sellername;


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

    public Integer getAttributeSetId() {
        return attributeSetId;
    }

    public void setAttributeSetId(Integer attributeSetId) {
        this.attributeSetId = attributeSetId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }



    public List<ProductLink> getProductLinks() {
        return productLinks;
    }

    public void setProductLinks(List<ProductLink> productLinks) {
        this.productLinks = productLinks;
    }

    public List<Object> getOptions() {
        return options;
    }

    public void setOptions(List<Object> options) {
        this.options = options;
    }

    public List<MediaGalleryEntry> getMediaGalleryEntries() {
        return mediaGalleryEntries;
    }

    public void setMediaGalleryEntries(List<MediaGalleryEntry> mediaGalleryEntries) {
        this.mediaGalleryEntries = mediaGalleryEntries;
    }

    public List<Object> getTierPrices() {
        return tierPrices;
    }

    public void setTierPrices(List<Object> tierPrices) {
        this.tierPrices = tierPrices;
    }




    public Boolean getIsProductEndorsed() {
        return isProductEndorsed;
    }

    public void setIsProductEndorsed(Boolean isProductEndorsed) {
        this.isProductEndorsed = isProductEndorsed;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }





    @SerializedName("custom_attributes")
    @Expose
    private List<SkuAttribute> skuAttributes = null;


    public List<SkuAttribute> getskuattribute() {
        return skuAttributes;
    }

    public void setCustomAttributes(List<SkuAttribute> skuAttributes) {
        this.skuAttributes = skuAttributes;
    }
}
