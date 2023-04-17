package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

public class AllHSNCode {

    public String hsncode_id;
    public String hsn_code;
    public String description_of_goods;
    public String cgst_rate;
    public String sgst_rate;
    public String igst_rate;

    public AllHSNCode(String hsncode_id, String hsn_code, String description_of_goods, String cgst_rate, String sgst_rate, String igst_rate) {
        this.hsncode_id = hsncode_id;
        this.hsn_code = hsn_code;
        this.description_of_goods = description_of_goods;
        this.cgst_rate = cgst_rate;
        this.sgst_rate = sgst_rate;
        this.igst_rate = igst_rate;
    }

    public String getHsncode_id() {
        return hsncode_id;
    }

    public void setHsncode_id(String hsncode_id) {
        this.hsncode_id = hsncode_id;
    }

    public String getHsn_code() {
        return hsn_code;
    }

    public void setHsn_code(String hsn_code) {
        this.hsn_code = hsn_code;
    }

    public String getDescription_of_goods() {
        return description_of_goods;
    }

    public void setDescription_of_goods(String description_of_goods) {
        this.description_of_goods = description_of_goods;
    }

    public String getCgst_rate() {
        return cgst_rate;
    }

    public void setCgst_rate(String cgst_rate) {
        this.cgst_rate = cgst_rate;
    }

    public String getSgst_rate() {
        return sgst_rate;
    }

    public void setSgst_rate(String sgst_rate) {
        this.sgst_rate = sgst_rate;
    }

    public String getIgst_rate() {
        return igst_rate;
    }

    public void setIgst_rate(String igst_rate) {
        this.igst_rate = igst_rate;
    }
}
