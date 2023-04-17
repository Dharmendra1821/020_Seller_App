package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class UdharCustomerModel {

    private String transaction_id;
    private String order_increment_id;
    private String customer_name;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    private String created_at;
    private String pending_amount;
    private String paid_amount;
    private String totalPaidAmount;

    public String getTotalPaidAmount() {
        return totalPaidAmount;
    }

    public void setTotalPaidAmount(String totalPaidAmount) {
        this.totalPaidAmount = totalPaidAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    private String summary;
    private String transactionType;

    public UdharCustomerModel(String transaction_id, String order_increment_id, String customer_name, String created_at, String pending_amount, String paid_amount,String summary,String transactionType,
                              String totalPaidAmount) {
        this.transaction_id = transaction_id;
        this.order_increment_id = order_increment_id;
        this.customer_name = customer_name;
        this.created_at = created_at;
        this.pending_amount = pending_amount;
        this.paid_amount = paid_amount;
        this.summary = summary;
        this.transactionType = transactionType;
        this.totalPaidAmount = totalPaidAmount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOrder_increment_id() {
        return order_increment_id;
    }

    public void setOrder_increment_id(String order_increment_id) {
        this.order_increment_id = order_increment_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPending_amount() {
        return pending_amount;
    }

    public void setPending_amount(String pending_amount) {
        this.pending_amount = pending_amount;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }
}
