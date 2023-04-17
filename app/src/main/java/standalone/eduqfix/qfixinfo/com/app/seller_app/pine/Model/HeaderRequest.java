package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model;

import com.google.gson.annotations.SerializedName;

import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Utility.GsonUtils;


public class HeaderRequest<T> {

    @SerializedName("Header")
    private Header Header;
    @SerializedName("Detail")
    private T Detail;

    public HeaderRequest(String methodId) {
        this.Header = new Header("93f2e3e35300415c8812ce1013e8e731", methodId, "userId", "1.0");
    }

    public Header getHeader() {
        return Header;
    }

    public void setHeader(Header header) {
        Header = header;
    }

    public T getDetail() {
        return Detail;
    }

    public void setDetail(T detail) {
        Detail = detail;
    }

    @Override
    public String toString() {
        return GsonUtils.fromJsonToString(this);
    }
}
