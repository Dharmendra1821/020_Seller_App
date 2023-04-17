package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import androidx.annotation.NonNull;

public class ProductListRequest {

    @NonNull
    private String seller_id;
    @NonNull
    private String page;
    @NonNull
    private String limit;
    @NonNull
    private String categoryId;

    @NonNull
    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(@NonNull String seller_id) {
        this.seller_id = seller_id;
    }

    @NonNull
    public String getPage() {
        return page;
    }

    public void setPage(@NonNull String page) {
        this.page = page;
    }

    @NonNull
    public String getLimit() {
        return limit;
    }

    public void setLimit(@NonNull String limit) {
        this.limit = limit;
    }

    @NonNull
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull String categoryId) {
        this.categoryId = categoryId;
    }
}
