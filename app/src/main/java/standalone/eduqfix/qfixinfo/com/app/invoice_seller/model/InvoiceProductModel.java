package standalone.eduqfix.qfixinfo.com.app.invoice_seller.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product")
public class InvoiceProductModel {

        @PrimaryKey(autoGenerate = true)
        int id;
        String invoiceId;
        String name;
        String price;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    String qty;
    String tax;

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    String cgst;
    String igst;
    String cgstValue;

    public String getMainTextValue() {
        return mainTextValue;
    }

    public void setMainTextValue(String mainTextValue) {
        this.mainTextValue = mainTextValue;
    }

    String mainTextValue;

    public String getCgstValue() {
        return cgstValue;
    }

    public void setCgstValue(String cgstValue) {
        this.cgstValue = cgstValue;
    }

    public String getIgstValue() {
        return igstValue;
    }

    public void setIgstValue(String igstValue) {
        this.igstValue = igstValue;
    }

    public InvoiceProductModel() {
    }

    String igstValue;

    public InvoiceProductModel(String invoiceId,String name, String price, String qty, String tax,String cgstValue,String mainTextValue) {
        this.invoiceId = invoiceId;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.tax = tax;
        this.cgstValue = cgstValue;
        this.mainTextValue = mainTextValue;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }
}
