package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddProductRequestNew {

    @SerializedName("approve_status")
    private int approve_status;
    @SerializedName("attributeSetId")
    private String attributeSetId;
    @SerializedName("backorders")
    private int backorders;
    @SerializedName("category_ids")
    private ArrayList<String> category_ids;
    @SerializedName("customAttributes")
    ArrayList <Object> customAttributes = new ArrayList <Object> ();
    @SerializedName("description")
    private String description;
    @SerializedName("enableQtyIncrements")
    private boolean enableQtyIncrements;

    public int getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(int approve_status) {
        this.approve_status = approve_status;
    }

    public String getAttributeSetId() {
        return attributeSetId;
    }

    public void setAttributeSetId(String attributeSetId) {
        this.attributeSetId = attributeSetId;
    }

    public int getBackorders() {
        return backorders;
    }

    public void setBackorders(int backorders) {
        this.backorders = backorders;
    }

    public ArrayList<String> getCategory_ids() {
        return category_ids;
    }

    public void setCategory_ids(ArrayList<String> category_ids) {
        this.category_ids = category_ids;
    }

    public ArrayList<Object> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(ArrayList<Object> customAttributes) {
        this.customAttributes = customAttributes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnableQtyIncrements() {
        return enableQtyIncrements;
    }

    public void setEnableQtyIncrements(boolean enableQtyIncrements) {
        this.enableQtyIncrements = enableQtyIncrements;
    }

    public int getHsn_code() {
        return hsn_code;
    }

    public void setHsn_code(int hsn_code) {
        this.hsn_code = hsn_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_in_stock() {
        return is_in_stock;
    }

    public void setIs_in_stock(String is_in_stock) {
        this.is_in_stock = is_in_stock;
    }

    public boolean isDecimalDivided() {
        return isDecimalDivided;
    }

    public void setDecimalDivided(boolean decimalDivided) {
        isDecimalDivided = decimalDivided;
    }

    public boolean isInStock() {
        return isInStock;
    }

    public void setInStock(boolean inStock) {
        isInStock = inStock;
    }

    public boolean isQtyDecimal() {
        return isQtyDecimal;
    }

    public void setQtyDecimal(boolean qtyDecimal) {
        isQtyDecimal = qtyDecimal;
    }

    public String getManage_stock() {
        return manage_stock;
    }

    public void setManage_stock(String manage_stock) {
        this.manage_stock = manage_stock;
    }

    public boolean isManageStock() {
        return manageStock;
    }

    public void setManageStock(boolean manageStock) {
        this.manageStock = manageStock;
    }

    public int getMaxSaleQty() {
        return maxSaleQty;
    }

    public void setMaxSaleQty(int maxSaleQty) {
        this.maxSaleQty = maxSaleQty;
    }

    public ArrayList<Object> getMedia_gallery_entries() {
        return media_gallery_entries;
    }

    public void setMedia_gallery_entries(ArrayList<Object> media_gallery_entries) {
        this.media_gallery_entries = media_gallery_entries;
    }

    public int getMinQty() {
        return minQty;
    }

    public void setMinQty(int minQty) {
        this.minQty = minQty;
    }

    public int getMinSaleQty() {
        return minSaleQty;
    }

    public void setMinSaleQty(int minSaleQty) {
        this.minSaleQty = minSaleQty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNotifyStockQty() {
        return notifyStockQty;
    }

    public void setNotifyStockQty(int notifyStockQty) {
        this.notifyStockQty = notifyStockQty;
    }

    public int getOld_status() {
        return old_status;
    }

    public void setOld_status(int old_status) {
        this.old_status = old_status;
    }

    public ArrayList<Object> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Object> options) {
        this.options = options;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProduct_has_weight() {
        return product_has_weight;
    }

    public void setProduct_has_weight(String product_has_weight) {
        this.product_has_weight = product_has_weight;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public int getQtyIncrements() {
        return qtyIncrements;
    }

    public void setQtyIncrements(int qtyIncrements) {
        this.qtyIncrements = qtyIncrements;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getStockStatusChangedAuto() {
        return stockStatusChangedAuto;
    }

    public void setStockStatusChangedAuto(int stockStatusChangedAuto) {
        this.stockStatusChangedAuto = stockStatusChangedAuto;
    }

    public String getSwatch_image() {
        return swatch_image;
    }

    public void setSwatch_image(String swatch_image) {
        this.swatch_image = swatch_image;
    }

    public int getTax_class_id() {
        return tax_class_id;
    }

    public void setTax_class_id(int tax_class_id) {
        this.tax_class_id = tax_class_id;
    }

    public ArrayList<Object> getTierPrices() {
        return tierPrices;
    }

    public void setTierPrices(ArrayList<Object> tierPrices) {
        this.tierPrices = tierPrices;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUse_config_manage_stock() {
        return use_config_manage_stock;
    }

    public void setUse_config_manage_stock(String use_config_manage_stock) {
        this.use_config_manage_stock = use_config_manage_stock;
    }

    public boolean isUseConfigBackorders() {
        return useConfigBackorders;
    }

    public void setUseConfigBackorders(boolean useConfigBackorders) {
        this.useConfigBackorders = useConfigBackorders;
    }

    public boolean isUseConfigEnableQtyInc() {
        return useConfigEnableQtyInc;
    }

    public void setUseConfigEnableQtyInc(boolean useConfigEnableQtyInc) {
        this.useConfigEnableQtyInc = useConfigEnableQtyInc;
    }

    public boolean isUseConfigManageStock() {
        return useConfigManageStock;
    }

    public void setUseConfigManageStock(boolean useConfigManageStock) {
        this.useConfigManageStock = useConfigManageStock;
    }

    public boolean isUseConfigMaxSaleQty() {
        return useConfigMaxSaleQty;
    }

    public void setUseConfigMaxSaleQty(boolean useConfigMaxSaleQty) {
        this.useConfigMaxSaleQty = useConfigMaxSaleQty;
    }

    public boolean isUseConfigMinQty() {
        return useConfigMinQty;
    }

    public void setUseConfigMinQty(boolean useConfigMinQty) {
        this.useConfigMinQty = useConfigMinQty;
    }

    public int getUseConfigMinSaleQty() {
        return useConfigMinSaleQty;
    }

    public void setUseConfigMinSaleQty(int useConfigMinSaleQty) {
        this.useConfigMinSaleQty = useConfigMinSaleQty;
    }

    public boolean isUseConfigNotifyStockQty() {
        return useConfigNotifyStockQty;
    }

    public void setUseConfigNotifyStockQty(boolean useConfigNotifyStockQty) {
        this.useConfigNotifyStockQty = useConfigNotifyStockQty;
    }

    public boolean isUseConfigQtyIncrements() {
        return useConfigQtyIncrements;
    }

    public void setUseConfigQtyIncrements(boolean useConfigQtyIncrements) {
        this.useConfigQtyIncrements = useConfigQtyIncrements;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @SerializedName("hsn_code")
    private int hsn_code;
    @SerializedName("id")
    private String id;
    @SerializedName("is_in_stock")
    private String is_in_stock;
    @SerializedName("isDecimalDivided")
    private boolean isDecimalDivided;
    @SerializedName("isInStock")
    private boolean isInStock;
    @SerializedName("isQtyDecimal")
    private boolean isQtyDecimal;
    @SerializedName("manage_stock")
    private String manage_stock;
    @SerializedName("manageStock")
    private boolean manageStock;
    @SerializedName("maxSaleQty")
    private int maxSaleQty;
    @SerializedName("media_gallery_entries")
    ArrayList <Object> media_gallery_entries = new ArrayList <Object> ();
    @SerializedName("minQty")
    private int minQty;
    @SerializedName("minSaleQty")
    private int minSaleQty;
    @SerializedName("name")
    private String name;
    @SerializedName("notifyStockQty")
    private int notifyStockQty;
    @SerializedName("old_status")
    private int old_status;
    @SerializedName("options")
    ArrayList <Object> options = new ArrayList <Object> ();
    @SerializedName("price")
    private int price;
    @SerializedName("product_has_weight")
    private String product_has_weight;
    @SerializedName("product_id")
    private String product_id;
    @SerializedName("qty")
    private String qty;
    @SerializedName("qtyIncrements")
    private int qtyIncrements;
    @SerializedName("seller_id")
    private String seller_id;
    @SerializedName("set")
    private int set;
    @SerializedName("product_type")
    private int product_type;
    @SerializedName("category_level_1")
    private String category_level_1;

    public String getCategory_level_1() {
        return category_level_1;
    }

    public void setCategory_level_1(String category_level_1) {
        this.category_level_1 = category_level_1;
    }

    public String getCategory_level_2() {
        return category_level_2;
    }

    public void setCategory_level_2(String category_level_2) {
        this.category_level_2 = category_level_2;
    }

    @SerializedName("category_level_2")
    private String category_level_2;

    public int getProduct_type() {
        return product_type;
    }

    public void setProduct_type(int product_type) {
        this.product_type = product_type;
    }

    @SerializedName("sku")
    private String sku;
    @SerializedName("status")
    private int status;
    @SerializedName("stockId")
    private int stockId;
    @SerializedName("stockStatusChangedAuto")
    private int stockStatusChangedAuto;
    @SerializedName("swatch_image")
    private String swatch_image;
    @SerializedName("tax_class_id")
    private int tax_class_id;
    @SerializedName("tierPrices")
    ArrayList <Object> tierPrices = new ArrayList <Object> ();
    @SerializedName("type")
    private String type;
    @SerializedName("typeId")
    private String typeId;
    @SerializedName("use_config_manage_stock")
    private String use_config_manage_stock;
    @SerializedName("useConfigBackorders")
    private boolean useConfigBackorders;
    @SerializedName("useConfigEnableQtyInc")
    private boolean useConfigEnableQtyInc;
    @SerializedName("useConfigManageStock")
    private boolean useConfigManageStock;
    @SerializedName("useConfigMaxSaleQty")
    private boolean useConfigMaxSaleQty;
    @SerializedName("useConfigMinQty")
    private boolean useConfigMinQty;
    @SerializedName("useConfigMinSaleQty")
    private int useConfigMinSaleQty;
    @SerializedName("useConfigNotifyStockQty")
    private boolean useConfigNotifyStockQty;
    @SerializedName("useConfigQtyIncrements")
    private boolean useConfigQtyIncrements;
    @SerializedName("visibility")
    private int visibility;
    @SerializedName("weight")
    private int weight;


}
