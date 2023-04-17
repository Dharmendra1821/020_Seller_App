package standalone.eduqfix.qfixinfo.com.app.seller_app.model;
import java.util.List;
public class Root
{
    private List<Items> items;

    private Search_criteria search_criteria;

    private int total_count;

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
    public void setTotal_count(int total_count){
        this.total_count = total_count;
    }
    public int getTotal_count(){
        return this.total_count;
    }
}
