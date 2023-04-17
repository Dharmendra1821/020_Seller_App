package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import java.util.ArrayList;

public class Product {

    private String sku;
    private String name;
    private String attributeSetId;
    private float price;
    private float status;
    private int visibility;
    private String typeId;
    private int tax_class_id;
    private int hsn_code;

    public int getHsn_code() {
        return hsn_code;
    }

    public void setHsn_code(int hsn_code) {
        this.hsn_code = hsn_code;
    }

    public int getTax_class_id() {
        return tax_class_id;
    }

    public void setTax_class_id(int tax_class_id) {
        this.tax_class_id = tax_class_id;
    }

    private StockData stock_data;

    public StockData getStock_data() {
        return stock_data;
    }

    public void setStock_data(StockData stock_data) {
        this.stock_data = stock_data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;


    public String getProduct_has_weight() {
        return product_has_weight;
    }

    public void setProduct_has_weight(String product_has_weight) {
        this.product_has_weight = product_has_weight;
    }

    private float weight;
    private String image;
    private String product_id;
    private String product_has_weight;

    public QuantityStockStatus getQuantity_and_stock_status() {
        return quantity_and_stock_status;
    }

    public void setQuantity_and_stock_status(QuantityStockStatus quantity_and_stock_status) {
        this.quantity_and_stock_status = quantity_and_stock_status;
    }

    public QuantityStockStatus quantity_and_stock_status;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSmall_image() {
        return small_image;
    }

    public void setSmall_image(String small_image) {
        this.small_image = small_image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSwatch_image() {
        return swatch_image;
    }

    public void setSwatch_image(String swatch_image) {
        this.swatch_image = swatch_image;
    }

    private String small_image;
    private String thumbnail;
    private String swatch_image;

    public ArrayList<String> getCategory_ids() {
        return category_ids;
    }

    public void setCategory_ids(ArrayList<String> category_ids) {
        this.category_ids = category_ids;
    }

    private ArrayList<String> category_ids;
    ExtensionAttributes extensionAttributes;
    ArrayList <MediaGalleryEntries> media_gallery_entries = new ArrayList <MediaGalleryEntries> ();
    ArrayList <Object> options = new ArrayList <Object> ();
    ArrayList <Object> tierPrices = new ArrayList <Object> ();

    public ArrayList<MediaGalleryEntries> getMedia_gallery_entries() {
        return media_gallery_entries;
    }

    public void setMedia_gallery_entries(ArrayList<MediaGalleryEntries> media_gallery_entries) {
        this.media_gallery_entries = media_gallery_entries;
    }

    ArrayList <Object> customAttributes = new ArrayList <Object> ();

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

    public String getAttributeSetId() {
        return attributeSetId;
    }

    public void setAttributeSetId(String attributeSetId) {
        this.attributeSetId = attributeSetId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getStatus() {
        return status;
    }

    public void setStatus(float status) {
        this.status = status;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public ExtensionAttributes getExtensionAttributes() {
        return extensionAttributes;
    }

    public void setExtensionAttributes(ExtensionAttributes extensionAttributes) {
        this.extensionAttributes = extensionAttributes;
    }

    public ArrayList<Object> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Object> options) {
        this.options = options;
    }

    public ArrayList<Object> getTierPrices() {
        return tierPrices;
    }

    public void setTierPrices(ArrayList<Object> tierPrices) {
        this.tierPrices = tierPrices;
    }

    public ArrayList<Object> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(ArrayList<Object> customAttributes) {
        this.customAttributes = customAttributes;
    }
}
