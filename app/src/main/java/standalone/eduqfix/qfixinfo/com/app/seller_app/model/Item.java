package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 29/4/19.
 */

public class Item {

    @SerializedName("item_id")
    private Integer itemId;
    @SerializedName("price")
    private Double price;
    @SerializedName("base_price")
    private Double basePrice;
    @SerializedName("qty")
    private Integer qty;
    @SerializedName("row_total")
    private Double rowTotal;
    @SerializedName("base_row_total")
    private Double baseRowTotal;
    @SerializedName("row_total_with_discount")
    private Double rowTotalWithDiscount;
    @SerializedName("tax_amount")
    private Double taxAmount;
    @SerializedName("base_tax_amount")
    private Double baseTaxAmount;
    @SerializedName("tax_percent")
    private Double taxPercent;
    @SerializedName("discount_amount")
    private Double discountAmount;
    @SerializedName("base_discount_amount")
    private Double baseDiscountAmount;
    @SerializedName("discount_percent")
    private Double discountPercent;
    @SerializedName("price_incl_tax")
    private Double priceInclTax;
    @SerializedName("base_price_incl_tax")
    private Double basePriceInclTax;
    @SerializedName("row_total_incl_tax")
    private Double rowTotalInclTax;
    @SerializedName("base_row_total_incl_tax")
    private Double baseRowTotalInclTax;
    @SerializedName("options")
    private String options;
    @SerializedName("weee_tax_applied_amount")
    private Object weeeTaxAppliedAmount;
    @SerializedName("weee_tax_applied")
    private Object weeeTaxApplied;
    @SerializedName("name")
    private String name;



    @SerializedName("sku")
    private String sku;
    //Use For Place Order Only
    @SerializedName("product_id")
    private Integer productId;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getRowTotal() {
        return rowTotal;
    }

    public void setRowTotal(Double rowTotal) {
        this.rowTotal = rowTotal;
    }

    public Double getBaseRowTotal() {
        return baseRowTotal;
    }

    public void setBaseRowTotal(Double baseRowTotal) {
        this.baseRowTotal = baseRowTotal;
    }

    public Double getRowTotalWithDiscount() {
        return rowTotalWithDiscount;
    }

    public void setRowTotalWithDiscount(Double rowTotalWithDiscount) {
        this.rowTotalWithDiscount = rowTotalWithDiscount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getBaseTaxAmount() {
        return baseTaxAmount;
    }

    public void setBaseTaxAmount(Double baseTaxAmount) {
        this.baseTaxAmount = baseTaxAmount;
    }

    public Double getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(Double taxPercent) {
        this.taxPercent = taxPercent;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getBaseDiscountAmount() {
        return baseDiscountAmount;
    }

    public void setBaseDiscountAmount(Double baseDiscountAmount) {
        this.baseDiscountAmount = baseDiscountAmount;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Double getPriceInclTax() {
        return priceInclTax;
    }

    public void setPriceInclTax(Double priceInclTax) {
        this.priceInclTax = priceInclTax;
    }

    public Double getBasePriceInclTax() {
        return basePriceInclTax;
    }

    public void setBasePriceInclTax(Double basePriceInclTax) {
        this.basePriceInclTax = basePriceInclTax;
    }

    public Double getRowTotalInclTax() {
        return rowTotalInclTax;
    }

    public void setRowTotalInclTax(Double rowTotalInclTax) {
        this.rowTotalInclTax = rowTotalInclTax;
    }

    public Double getBaseRowTotalInclTax() {
        return baseRowTotalInclTax;
    }

    public void setBaseRowTotalInclTax(Double baseRowTotalInclTax) {
        this.baseRowTotalInclTax = baseRowTotalInclTax;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Object getWeeeTaxAppliedAmount() {
        return weeeTaxAppliedAmount;
    }

    public void setWeeeTaxAppliedAmount(Object weeeTaxAppliedAmount) {
        this.weeeTaxAppliedAmount = weeeTaxAppliedAmount;
    }

    public Object getWeeeTaxApplied() {
        return weeeTaxApplied;
    }

    public void setWeeeTaxApplied(Object weeeTaxApplied) {
        this.weeeTaxApplied = weeeTaxApplied;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
