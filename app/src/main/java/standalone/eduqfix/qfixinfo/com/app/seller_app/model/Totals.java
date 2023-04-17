package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darshan on 29/4/19.
 */

public class Totals {

    @SerializedName("grand_total")
    private Double grandTotal;
    @SerializedName("base_grand_total")
    private Double baseGrandTotal;
    @SerializedName("subtotal")
    private Double subtotal;
    @SerializedName("base_subtotal")
    private Double baseSubtotal;
    @SerializedName("discount_amount")
    private Double discountAmount;
    @SerializedName("base_discount_amount")
    private Double baseDiscountAmount;
    @SerializedName("subtotal_with_discount")
    private Double subtotalWithDiscount;
    @SerializedName("base_subtotal_with_discount")
    private Double baseSubtotalWithDiscount;
    @SerializedName("shipping_amount")
    private Double shippingAmount;
    @SerializedName("base_shipping_amount")
    private Double baseShippingAmount;
    @SerializedName("shipping_discount_amount")
    private Double shippingDiscountAmount;
    @SerializedName("base_shipping_discount_amount")
    private Double baseShippingDiscountAmount;
    @SerializedName("tax_amount")
    private Double taxAmount;
    @SerializedName("base_tax_amount")
    private Double baseTaxAmount;
    @SerializedName("weee_tax_applied_amount")
    private Object weeeTaxAppliedAmount;
    @SerializedName("shipping_tax_amount")
    private Double shippingTaxAmount;
    @SerializedName("base_shipping_tax_amount")
    private Double baseShippingTaxAmount;
    @SerializedName("subtotal_incl_tax")
    private Double subtotalInclTax;
    @SerializedName("shipping_incl_tax")
    private Double shippingInclTax;
    @SerializedName("base_shipping_incl_tax")
    private Double baseShippingInclTax;
    @SerializedName("base_currency_code")
    private String baseCurrencyCode;
    @SerializedName("quote_currency_code")
    private String quoteCurrencyCode;
    @SerializedName("items_qty")
    private Integer itemsQty;
    @SerializedName("total_segments")
    private List<TotalSegment> totalSegments = null;
    @SerializedName("items")
    private List<Item> items = null;

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getBaseGrandTotal() {
        return baseGrandTotal;
    }

    public void setBaseGrandTotal(Double baseGrandTotal) {
        this.baseGrandTotal = baseGrandTotal;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getBaseSubtotal() {
        return baseSubtotal;
    }

    public void setBaseSubtotal(Double baseSubtotal) {
        this.baseSubtotal = baseSubtotal;
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

    public Double getSubtotalWithDiscount() {
        return subtotalWithDiscount;
    }

    public void setSubtotalWithDiscount(Double subtotalWithDiscount) {
        this.subtotalWithDiscount = subtotalWithDiscount;
    }

    public Double getBaseSubtotalWithDiscount() {
        return baseSubtotalWithDiscount;
    }

    public void setBaseSubtotalWithDiscount(Double baseSubtotalWithDiscount) {
        this.baseSubtotalWithDiscount = baseSubtotalWithDiscount;
    }

    public Double getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(Double shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public Double getBaseShippingAmount() {
        return baseShippingAmount;
    }

    public void setBaseShippingAmount(Double baseShippingAmount) {
        this.baseShippingAmount = baseShippingAmount;
    }

    public Double getShippingDiscountAmount() {
        return shippingDiscountAmount;
    }

    public void setShippingDiscountAmount(Double shippingDiscountAmount) {
        this.shippingDiscountAmount = shippingDiscountAmount;
    }

    public Double getBaseShippingDiscountAmount() {
        return baseShippingDiscountAmount;
    }

    public void setBaseShippingDiscountAmount(Double baseShippingDiscountAmount) {
        this.baseShippingDiscountAmount = baseShippingDiscountAmount;
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

    public Object getWeeeTaxAppliedAmount() {
        return weeeTaxAppliedAmount;
    }

    public void setWeeeTaxAppliedAmount(Object weeeTaxAppliedAmount) {
        this.weeeTaxAppliedAmount = weeeTaxAppliedAmount;
    }

    public Double getShippingTaxAmount() {
        return shippingTaxAmount;
    }

    public void setShippingTaxAmount(Double shippingTaxAmount) {
        this.shippingTaxAmount = shippingTaxAmount;
    }

    public Double getBaseShippingTaxAmount() {
        return baseShippingTaxAmount;
    }

    public void setBaseShippingTaxAmount(Double baseShippingTaxAmount) {
        this.baseShippingTaxAmount = baseShippingTaxAmount;
    }

    public Double getSubtotalInclTax() {
        return subtotalInclTax;
    }

    public void setSubtotalInclTax(Double subtotalInclTax) {
        this.subtotalInclTax = subtotalInclTax;
    }

    public Double getShippingInclTax() {
        return shippingInclTax;
    }

    public void setShippingInclTax(Double shippingInclTax) {
        this.shippingInclTax = shippingInclTax;
    }

    public Double getBaseShippingInclTax() {
        return baseShippingInclTax;
    }

    public void setBaseShippingInclTax(Double baseShippingInclTax) {
        this.baseShippingInclTax = baseShippingInclTax;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getQuoteCurrencyCode() {
        return quoteCurrencyCode;
    }

    public void setQuoteCurrencyCode(String quoteCurrencyCode) {
        this.quoteCurrencyCode = quoteCurrencyCode;
    }

    public Integer getItemsQty() {
        return itemsQty;
    }

    public void setItemsQty(Integer itemsQty) {
        this.itemsQty = itemsQty;
    }

    public List<TotalSegment> getTotalSegments() {
        return totalSegments;
    }

    public void setTotalSegments(List<TotalSegment> totalSegments) {
        this.totalSegments = totalSegments;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
