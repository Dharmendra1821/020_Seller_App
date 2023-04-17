package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model;

import com.google.gson.annotations.SerializedName;


import java.util.List;


public class InitiatePaymentRequest {


        @SerializedName("customer_name")
        private String customerName;
        @SerializedName("customer_address")
        private String customerAddress;
        @SerializedName("customer_city")
        private String customerCity;
        @SerializedName("customer_state")
        private String customerState;
        @SerializedName("customer_pincode")
        private String customerPincode;
        @SerializedName("customer_country")
        private String customerCountry;
        @SerializedName("customer_phone")
        private String customerPhone;
        @SerializedName("customer_email")
        private String customerEmail;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("amount")
        private Double amount;
        @SerializedName("payload")
        private List<Payload> payload = null;
        @SerializedName("identifier")
        private String identifier;
        @SerializedName("channel")
        private String channel;
        @SerializedName("currency_code")
        private String currencyCode;
        @SerializedName("return_url")
        private String returnUrl;

        private String environment;

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerAddress() {
            return customerAddress;
        }

        public void setCustomerAddress(String customerAddress) {
            this.customerAddress = customerAddress;
        }

        public String getCustomerCity() {
            return customerCity;
        }

        public void setCustomerCity(String customerCity) {
            this.customerCity = customerCity;
        }

        public String getCustomerState() {
            return customerState;
        }

        public void setCustomerState(String customerState) {
            this.customerState = customerState;
        }

        public String getCustomerPincode() {
            return customerPincode;
        }

        public void setCustomerPincode(String customerPincode) {
            this.customerPincode = customerPincode;
        }

        public String getCustomerCountry() {
            return customerCountry;
        }

        public void setCustomerCountry(String customerCountry) {
            this.customerCountry = customerCountry;
        }

        public String getCustomerPhone() {
            return customerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public List<Payload> getPayload() {
            return payload;
        }

        public void setPayload(List<Payload> payload) {
            this.payload = payload;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getReturnUrl() {
            return returnUrl;
        }

        public void setReturnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
        }

        public String getEnvironment() {
            return environment;
        }

        public void setEnvironment(String environment) {
            this.environment = environment;
        }


}
