package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model;

public enum PaymentModeEnum {


    UPI("UPI", new DoTransactionRequest(5120L)),
    Sale("Card", new DoTransactionRequest(4001L)),
    Bharat_QR("Bharat QR", new DoTransactionRequest(5123L)),
    UPI_GET_STATUS("UPI_GET_STATUS", new DoTransactionRequest(5122L));

    private final String name;
    private HeaderRequest<DoTransactionRequest> request;

    PaymentModeEnum(String s, DoTransactionRequest doTransactionRequest) {
        this.name = s;
        request = new HeaderRequest<>("1001");
        request.setDetail(doTransactionRequest);
    }

    public HeaderRequest<DoTransactionRequest> getRequest() {
        return request;
    }

    public String getName() {
        return name;
    }
}
