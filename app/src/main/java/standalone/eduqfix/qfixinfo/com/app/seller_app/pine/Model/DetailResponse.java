package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model;

import com.google.gson.annotations.SerializedName;

public class DetailResponse<T>{

    @SerializedName("Detail")
    private T Detail;

    @SerializedName("Header")
    private Header Header;

    @SerializedName("Response")
    private Response Response;

    public T getDetail() {
        return Detail;
    }

    public void setDetail(T detail) {
        Detail = detail;
    }

    public standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.Header getHeader() {
        return Header;
    }

    public void setHeader(standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.Header header) {
        Header = header;
    }

    public Response getResponse() {
        return Response;
    }

    public void setResponse(Response response) {
        Response = response;
    }
}

