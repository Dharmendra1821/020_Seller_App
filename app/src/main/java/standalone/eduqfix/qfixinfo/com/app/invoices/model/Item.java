package standalone.eduqfix.qfixinfo.com.app.invoices.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by darshan on 27/2/19.
 */

public class Item implements Serializable {

    @SerializedName("base_discount_tax_compensation_amount")
    private Double baseDiscountTaxCompensationAmount;
    @SerializedName("base_price")
    private Double basePrice;
    @SerializedName("base_price_incl_tax")
    private Double basePriceInclTax;
    @SerializedName("base_row_total")
    private Double baseRowTotal;
    @SerializedName("base_row_total_incl_tax")
    private Double baseRowTotalInclTax;
    @SerializedName("base_tax_amount")
    private Double baseTaxAmount;
    @SerializedName("entity_id")
    private Integer entityId;
    @SerializedName("discount_tax_compensation_amount")
    private Double discountTaxCompensationAmount;
    @SerializedName("name")
    private String name;
    @SerializedName("parent_id")
    private Integer parentId;
    @SerializedName("price")
    private Double price;
    @SerializedName("price_incl_tax")
    private Double priceInclTax;
    @SerializedName("product_id")
    private Integer productId;
    @SerializedName("row_total")
    private Double rowTotal;
    @SerializedName("row_total_incl_tax")
    private Double rowTotalInclTax;
    @SerializedName("sku")
    private String sku;
    @SerializedName("tax_amount")
    private Double taxAmount;
    @SerializedName("order_item_id")
    private Integer orderItemId;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    @SerializedName("qty")
    private String qty;
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("amount")
    private String amount;

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Double getBaseDiscountTaxCompensationAmount() {
        return baseDiscountTaxCompensationAmount;
    }

    public void setBaseDiscountTaxCompensationAmount(Double baseDiscountTaxCompensationAmount) {
        this.baseDiscountTaxCompensationAmount = baseDiscountTaxCompensationAmount;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getBasePriceInclTax() {
        return basePriceInclTax;
    }

    public void setBasePriceInclTax(Double basePriceInclTax) {
        this.basePriceInclTax = basePriceInclTax;
    }

    public Double getBaseRowTotal() {
        return baseRowTotal;
    }

    public void setBaseRowTotal(Double baseRowTotal) {
        this.baseRowTotal = baseRowTotal;
    }

    public Double getBaseRowTotalInclTax() {
        return baseRowTotalInclTax;
    }

    public void setBaseRowTotalInclTax(Double baseRowTotalInclTax) {
        this.baseRowTotalInclTax = baseRowTotalInclTax;
    }

    public Double getBaseTaxAmount() {
        return baseTaxAmount;
    }

    public void setBaseTaxAmount(Double baseTaxAmount) {
        this.baseTaxAmount = baseTaxAmount;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Double getDiscountTaxCompensationAmount() {
        return discountTaxCompensationAmount;
    }

    public void setDiscountTaxCompensationAmount(Double discountTaxCompensationAmount) {
        this.discountTaxCompensationAmount = discountTaxCompensationAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPriceInclTax() {
        return priceInclTax;
    }

    public void setPriceInclTax(Double priceInclTax) {
        this.priceInclTax = priceInclTax;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Double getRowTotal() {
        return rowTotal;
    }

    public void setRowTotal(Double rowTotal) {
        this.rowTotal = rowTotal;
    }

    public Double getRowTotalInclTax() {
        return rowTotalInclTax;
    }

    public void setRowTotalInclTax(Double rowTotalInclTax) {
        this.rowTotalInclTax = rowTotalInclTax;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }


}
