package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class SellerInvoiceModel {

    private String invoice_id;
    private String invoicenumber;
    private String cname;
    private String gstin;
    private String invoiceDate;
    private String mobile_number;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private String email;
    private String amount;
    private String expiryDate;
    private String paymentstatus;
    private String receipt_link;
    private boolean isSelected;

    public SellerInvoiceModel(String invoice_id, String invoicenumber, String cname, String gstin, String invoiceDate, String mobile_number, String email, String amount, String expiryDate, String paymentstatus, String receipt_link) {
        this.invoice_id = invoice_id;
        this.invoicenumber = invoicenumber;
        this.cname = cname;
        this.gstin = gstin;
        this.invoiceDate = invoiceDate;
        this.mobile_number = mobile_number;
        this.email = email;
        this.amount = amount;
        this.expiryDate = expiryDate;
        this.paymentstatus = paymentstatus;
        this.receipt_link = receipt_link;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getInvoicenumber() {
        return invoicenumber;
    }

    public void setInvoicenumber(String invoicenumber) {
        this.invoicenumber = invoicenumber;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getReceipt_link() {
        return receipt_link;
    }

    public void setReceipt_link(String receipt_link) {
        this.receipt_link = receipt_link;
    }
}
