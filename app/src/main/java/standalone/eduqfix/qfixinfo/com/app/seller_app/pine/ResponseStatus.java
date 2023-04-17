package standalone.eduqfix.qfixinfo.com.app.seller_app.pine;

public enum ResponseStatus {
    SUCCESS(0), FAILED(1);

    private final int value;

    ResponseStatus(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
