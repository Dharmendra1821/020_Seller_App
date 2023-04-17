package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import java.util.List;

public class SubCategories {
    public List<Items> items;

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public Search_criteria getSearch_criteria() {
        return search_criteria;
    }

    public void setSearch_criteria(Search_criteria search_criteria) {
        this.search_criteria = search_criteria;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public Search_criteria search_criteria;
    public int total_count;
}
