package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model;


import com.google.gson.annotations.SerializedName;


public class DoTransactionRequest {

    public DoTransactionRequest(Long transactionType) {
        TransactionType = transactionType;
    }

    @SerializedName("Field0")
    private String Field0;

    @SerializedName("Field1")
    private String Field1;

    @SerializedName("Field2")
    private String Field2;

    @SerializedName("Field3")
    private String Field3;

    @SerializedName("BillingRefNo")
    private String BillingRefNo;

    @SerializedName("BankCode")
    private String BankCode;

    @SerializedName("CardNumber")
    private String CardNumber;

    @SerializedName("Expiry")
    private String Expiry;

    @SerializedName("CustomerMobileNumber")
    private String CustomerMobileNumber;

    @SerializedName("CustomerEmailId")
    private String CustomerEmailId;

    @SerializedName("MerchantMobileNumber")
    private String MerchantMobileNumber;

    @SerializedName("MerchantEmailId")
    private String MerchantEmailId;

    public Long getTransactionLogId() {
        return TransactionLogId;
    }

    @SerializedName("CurrencyId")

    private String CurrencyId;

    @SerializedName("TransactionType")
    private Long TransactionType;

    @SerializedName("PaymentAmount")
    private double PaymentAmount;

    @SerializedName("InvoiceNo")
    private Long InvoiceNo;

    @SerializedName("TransactionLogId")
    private Long TransactionLogId;

    @SerializedName("RewardAmount")
    private Long RewardAmount;

    @SerializedName("WalletProgramId")
    private Long WalletProgramId;

    @SerializedName("BatchNo")
    private Integer BatchNo;

    @SerializedName("Roc")
    private Integer Roc;

    @SerializedName("IsSwipe")
    private Boolean IsSwipe;

    @SerializedName("ConsentCustomerMobile")
    private Boolean ConsentCustomerMobile;

    @SerializedName("ConsentCustomerEmailId")
    private Boolean ConsentCustomerEmailId;

    @SerializedName("ConsentMerchantMobile")
    private Boolean ConsentMerchantMobile;

    @SerializedName("ConsentMerchantEmailId")
    private Boolean ConsentMerchantEmailId;

    public void setField0(String field0) {
        Field0 = field0;
    }

    public DoTransactionRequest setField1(String field1) {
        Field1 = field1;
        return this;
    }

    public void setField2(String field2) {
        Field2 = field2;
    }

    public void setField3(String field3) {
        Field3 = field3;
    }

    public DoTransactionRequest setBillingRefNo(String billingRefNo) {
        BillingRefNo = billingRefNo;
        return this;
    }

    public DoTransactionRequest setBankCode(String bankCode) {
        BankCode = bankCode;
        return this;
    }

    public DoTransactionRequest setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
        return this;
    }

    public DoTransactionRequest setExpiry(String expiry) {
        Expiry = expiry;
        return this;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        CustomerMobileNumber = customerMobileNumber;
    }

    public void setCustomerEmailId(String customerEmailId) {
        CustomerEmailId = customerEmailId;
    }

    public void setMerchantMobileNumber(String merchantMobileNumber) {
        MerchantMobileNumber = merchantMobileNumber;
    }

    public void setMerchantEmailId(String merchantEmailId) {
        MerchantEmailId = merchantEmailId;
    }

    public void setCurrencyId(String currencyId) {
        CurrencyId = currencyId;
    }

    public void setTransactionType(Long transactionType) {
        TransactionType = transactionType;
    }

    public DoTransactionRequest setPaymentAmount(double paymentAmount) {
        PaymentAmount = paymentAmount;
        return this;
    }

    public DoTransactionRequest setInvoiceNo(Long invoiceNo) {
        InvoiceNo = invoiceNo;
        return this;
    }

    public DoTransactionRequest setTransactionLogId(Long transactionLogId) {
        TransactionLogId = transactionLogId;
        return this;
    }

    public DoTransactionRequest setRewardAmount(Long rewardAmount) {
        RewardAmount = rewardAmount;
        return this;
    }

    public void setWalletProgramId(Long walletProgramId) {
        WalletProgramId = walletProgramId;
    }

    public void setBatchNo(Integer batchNo) {
        BatchNo = batchNo;
    }

    public void setRoc(Integer roc) {
        Roc = roc;
    }

    public DoTransactionRequest setSwipe(Boolean swipe) {
        IsSwipe = swipe;
        return this;
    }

    public void setConsentCustomerMobile(Boolean consentCustomerMobile) {
        ConsentCustomerMobile = consentCustomerMobile;
    }

    public void setConsentCustomerEmailId(Boolean consentCustomerEmailId) {
        ConsentCustomerEmailId = consentCustomerEmailId;
    }

    public void setConsentMerchantMobile(Boolean consentMerchantMobile) {
        ConsentMerchantMobile = consentMerchantMobile;
    }

    public void setConsentMerchantEmailId(Boolean consentMerchantEmailId) {
        ConsentMerchantEmailId = consentMerchantEmailId;
    }

    public Object getValue(Object value) {
        return value == null ? "" : value;
    }


    public Long getInvoiceNo() {
        return InvoiceNo;
    }
}
