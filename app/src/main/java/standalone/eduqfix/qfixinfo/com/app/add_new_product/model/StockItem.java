package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

public class StockItem {

    private float stockId;
    private float qty;
    private boolean isInStock;
    private boolean isQtyDecimal;
    private boolean useConfigMinQty;
    private float minQty;
    private float useConfigMinSaleQty;
    private float minSaleQty;
    private boolean useConfigMaxSaleQty;

    public float getStockId() {
        return stockId;
    }

    public void setStockId(float stockId) {
        this.stockId = stockId;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
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

    public boolean isUseConfigMinQty() {
        return useConfigMinQty;
    }

    public void setUseConfigMinQty(boolean useConfigMinQty) {
        this.useConfigMinQty = useConfigMinQty;
    }

    public float getMinQty() {
        return minQty;
    }

    public void setMinQty(float minQty) {
        this.minQty = minQty;
    }

    public float getUseConfigMinSaleQty() {
        return useConfigMinSaleQty;
    }

    public void setUseConfigMinSaleQty(float useConfigMinSaleQty) {
        this.useConfigMinSaleQty = useConfigMinSaleQty;
    }

    public float getMinSaleQty() {
        return minSaleQty;
    }

    public void setMinSaleQty(float minSaleQty) {
        this.minSaleQty = minSaleQty;
    }

    public boolean isUseConfigMaxSaleQty() {
        return useConfigMaxSaleQty;
    }

    public void setUseConfigMaxSaleQty(boolean useConfigMaxSaleQty) {
        this.useConfigMaxSaleQty = useConfigMaxSaleQty;
    }

    public float getMaxSaleQty() {
        return maxSaleQty;
    }

    public void setMaxSaleQty(float maxSaleQty) {
        this.maxSaleQty = maxSaleQty;
    }

    public boolean isUseConfigBackorders() {
        return useConfigBackorders;
    }

    public void setUseConfigBackorders(boolean useConfigBackorders) {
        this.useConfigBackorders = useConfigBackorders;
    }

    public float getBackorders() {
        return backorders;
    }

    public void setBackorders(float backorders) {
        this.backorders = backorders;
    }

    public boolean isUseConfigNotifyStockQty() {
        return useConfigNotifyStockQty;
    }

    public void setUseConfigNotifyStockQty(boolean useConfigNotifyStockQty) {
        this.useConfigNotifyStockQty = useConfigNotifyStockQty;
    }

    public float getNotifyStockQty() {
        return notifyStockQty;
    }

    public void setNotifyStockQty(float notifyStockQty) {
        this.notifyStockQty = notifyStockQty;
    }

    public boolean isUseConfigQtyIncrements() {
        return useConfigQtyIncrements;
    }

    public void setUseConfigQtyIncrements(boolean useConfigQtyIncrements) {
        this.useConfigQtyIncrements = useConfigQtyIncrements;
    }

    public float getQtyIncrements() {
        return qtyIncrements;
    }

    public void setQtyIncrements(float qtyIncrements) {
        this.qtyIncrements = qtyIncrements;
    }

    public boolean isUseConfigEnableQtyInc() {
        return useConfigEnableQtyInc;
    }

    public void setUseConfigEnableQtyInc(boolean useConfigEnableQtyInc) {
        this.useConfigEnableQtyInc = useConfigEnableQtyInc;
    }

    public boolean isEnableQtyIncrements() {
        return enableQtyIncrements;
    }

    public void setEnableQtyIncrements(boolean enableQtyIncrements) {
        this.enableQtyIncrements = enableQtyIncrements;
    }

    public boolean isUseConfigManageStock() {
        return useConfigManageStock;
    }

    public void setUseConfigManageStock(boolean useConfigManageStock) {
        this.useConfigManageStock = useConfigManageStock;
    }

    public boolean isManageStock() {
        return manageStock;
    }

    public void setManageStock(boolean manageStock) {
        this.manageStock = manageStock;
    }

    public String getLowStockDate() {
        return lowStockDate;
    }

    public void setLowStockDate(String lowStockDate) {
        this.lowStockDate = lowStockDate;
    }

    public boolean isDecimalDivided() {
        return isDecimalDivided;
    }

    public void setDecimalDivided(boolean decimalDivided) {
        isDecimalDivided = decimalDivided;
    }

    public float getStockStatusChangedAuto() {
        return stockStatusChangedAuto;
    }

    public void setStockStatusChangedAuto(float stockStatusChangedAuto) {
        this.stockStatusChangedAuto = stockStatusChangedAuto;
    }

    public ExtensionAttributes getExtensionAttributesObject() {
        return ExtensionAttributesObject;
    }

    public void setExtensionAttributesObject(ExtensionAttributes extensionAttributesObject) {
        ExtensionAttributesObject = extensionAttributesObject;
    }

    private float maxSaleQty;
    private boolean useConfigBackorders;
    private float backorders;
    private boolean useConfigNotifyStockQty;
    private float notifyStockQty;
    private boolean useConfigQtyIncrements;
    private float qtyIncrements;
    private boolean useConfigEnableQtyInc;
    private boolean enableQtyIncrements;
    private boolean useConfigManageStock;
    private boolean manageStock;
    private String lowStockDate;
    private boolean isDecimalDivided;
    private float stockStatusChangedAuto;
    ExtensionAttributes ExtensionAttributesObject;

}
