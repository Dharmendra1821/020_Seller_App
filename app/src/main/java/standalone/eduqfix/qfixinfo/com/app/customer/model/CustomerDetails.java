package standalone.eduqfix.qfixinfo.com.app.customer.model;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Search_criteria;

public class CustomerDetails
{
    private List<Items> items;

    private Search_criteria search_criteria;

    private Integer total_count;

    public void setItems(List<Items> items){
        this.items = items;
    }
    public List<Items> getItems(){
        return this.items;
    }
    public void setSearch_criteria(Search_criteria search_criteria){
        this.search_criteria = search_criteria;
    }
    public Search_criteria getSearch_criteria(){
        return this.search_criteria;
    }
    public void setTotal_count(Integer total_count){
        this.total_count = total_count;
    }
    public Integer getTotal_count(){
        return this.total_count;
    }
}
